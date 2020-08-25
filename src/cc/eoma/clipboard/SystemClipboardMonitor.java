package cc.eoma.clipboard;

import cc.eoma.clipboard.example.ImageSelection;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.FileWriter;
import java.io.IOException;

public class SystemClipboardMonitor implements ClipboardOwner {

    public SystemClipboardMonitor() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(clipboard.getContents(null), this);
    }

    public static void main(String[] args) throws IOException {
        new SystemClipboardMonitor();
        while (true) {
            System.in.read();
        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor) || clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
            try {
                /*
                    如果是 IMAGE 信息时，在VirtualBox虚拟机中粘帖会报错，导致VirtualBox虚拟机异常关闭（在 其他地方未发现类似问题）;
                    因此检测到图片时，延迟5s（以便VritualBox虚拟机中粘帖图像）后在将剪切板重置为自己所有，便于后续监听剪切板信息变更。

                    另：使用 JHotKeys 监听 CTRL+C时，会阻断 系统 复制 动作，导致系统剪切板中不会存在要复制的信息（复制动作失效）。
                 */
                Thread.sleep(5000);

                Image image = (Image) clipboard.getData(DataFlavor.imageFlavor);
                ImageSelection imageSelection = new ImageSelection(image);
                clipboard.setContents(imageSelection, this);
            } catch (Exception e) {
                clipboard.setContents(contents, this);
            }
            return;
        }




        if (!clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            clipboard.setContents(contents, this);
            return;
        }
        String text = "";
        // 取出文本
        try {
            text = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("clipboard add new text:" + text);
        if (text.startsWith("@log")) {
            this.appendToFile("/home/goma/Desktop/xxxxx", text.substring(4, text.length()));
        }

        // @log存入剪贴板，并注册自己为所有者,用以监控下一次剪贴板内容变化
        StringSelection tmp = new StringSelection(text);
        clipboard.setContents(tmp, this);
    }

    private void appendToFile(String file, String text) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(text + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

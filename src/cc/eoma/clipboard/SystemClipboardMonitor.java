package cc.eoma.clipboard;

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

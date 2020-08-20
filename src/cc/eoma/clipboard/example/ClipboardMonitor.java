package cc.eoma.clipboard.example;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;


public class ClipboardMonitor implements ClipboardOwner {
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public ClipboardMonitor() {
        //如果剪贴板中有文本，则将它的ClipboardOwner设为自己  ;  每new 一个就是一个新实例，不同实例中的 自定义 selection 不可互转
//        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
        clipboard.setContents(clipboard.getContents(null), this);
//        }
    }


    public static void main(String[] args) throws IOException {
        new ClipboardMonitor();
        while (true) {
            System.in.read();
        }
    }

    /**
     * 通知此对象，它已不再是剪贴板所有者。当其他应用程序或此应用程序中的其他对象维护剪贴板的所有权时，调用此方法。
     *
     * @param clipboard 不再拥有的剪贴板
     * @param contents  此所有者置于剪贴板上的内容
     */
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

        if(text.startsWith("@log")){
            System.out.println("write " + text + "to file.");
        }

        // 存入剪贴板，并注册自己为所有者,用以监控下一次剪贴板内容变化
        StringSelection tmp = new StringSelection(text);
        clipboard.setContents(tmp, this);
    }
}

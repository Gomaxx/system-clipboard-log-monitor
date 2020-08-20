package cc.eoma.clipboard.example;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;

public class ClipboardExample  implements ClipboardOwner {
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public ClipboardExample() {
        //如果剪贴板中有文本，则将它的ClipboardOwner设为自己  ;  每new 一个就是一个新实例，不同实例中的 自定义 selection 不可互转
//        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){
        clipboard.setContents(clipboard.getContents(null), this);
//        }
    }





    /**
     * 把文本设置到剪贴板（复制）
     */
    public static void setClipboardString(String text) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }

    /**
     * 从剪贴板中获取文本（粘贴）
     */
    public static String getClipboardString() {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);
        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        Clipboard clipboard = toolkit.getSystemClipboard();

//        clipboard.setContents( new ImageSelection(toolkit.getImage("1.jpg")) , null );

        ClipboardExample ce = new ClipboardExample();

        Clipboard clipboard = ce.clipboard;

        File file = new File("/home/goma/Pictures/tongyibu.png");
        System.out.println(file.canRead());
        System.out.println(file.getName());

//        FileSelection fileSelection = new FileSelection(file);
//        clipboard.setContents(fileSelection, null);


        CustSelection custSelection = new CustSelection(file);
        clipboard.setContents(custSelection, null);


//        ClipboardExample.setClipboardString("god like.");

//        // 可以简单的监视系统剪切板里的内容变化，不过如果重复复制同一种类型的数据，比如复制了两次不同内容的文本，只会触发一次事件
//        clipboard.addFlavorListener(new FlavorListener() {
//            @Override
//            public void flavorsChanged(FlavorEvent e) {
//                System.out.println("====change");
//                Clipboard cb = (Clipboard)e.getSource();
//                Transferable trans = cb.getContents(null);
//                try {
//                    System.out.println(trans.getTransferData(DataFlavor.stringFlavor));
//                } catch (UnsupportedFlavorException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });


//        while (true) {
//            Thread.sleep(10);
////            System.out.println(getClipboardString());
//        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

        try {
            // 如果不暂停一下，经常会抛出IllegalStateException
            Thread.sleep(1);




            if (clipboard.isDataFlavorAvailable(CustSelection.rangeFlavor)) { // 这里仅仅为了检测这两种格式，没有什么意义
                File p = (File)clipboard.getData(CustSelection.rangeFlavor);
                System.out.println(p.getName());
            }






            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                // 取出文本
                String text = (String) clipboard.getData(DataFlavor.stringFlavor);
                System.out.println(text);
                // 存入剪贴板，并注册自己为所有者,用以监控下一次剪贴板内容变化
                StringSelection tmp = new StringSelection(text);
                clipboard.setContents(tmp, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

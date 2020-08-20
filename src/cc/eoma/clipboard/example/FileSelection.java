package cc.eoma.clipboard.example;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

public class FileSelection implements Transferable {
    private File file;

    public FileSelection(File file) {
        this.file = file;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{new DataFlavor(file.getClass(), DataFlavor.javaJVMLocalObjectMimeType)};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
//        return flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return file;
        } else {
            return null;
        }
    }
}

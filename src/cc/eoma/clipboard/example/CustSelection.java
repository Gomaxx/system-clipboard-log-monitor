package cc.eoma.clipboard.example;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;

import static jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants.RANGE;

public class CustSelection implements Transferable {
    public static final DataFlavor rangeFlavor = new DataFlavor(File.class, "Report Range");//class为自定义的java类 字串随便
    private static final DataFlavor[] flavors = {rangeFlavor};
    private Object data;

    public CustSelection(Object data) {
        this.data = data;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        boolean support = false;
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                support = true;
                break;
            }
        }

        if(support){
//        if (flavor.equals(flavors[RANGE])) {
            return data;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) flavors.clone();
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }


}

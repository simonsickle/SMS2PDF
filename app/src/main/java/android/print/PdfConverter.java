package android.print;

import android.content.Context;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;

public class PdfConverter {

    private static final String TAG = PdfConverter.class.getSimpleName();
    private static final int DP_VALUE = 300;


    public void print(final PrintDocumentAdapter printAdapter, final File path, final String fileName, final CallbackPrint callback) {
        printAdapter.onLayout(null, getDefaultPrintAttrs(), null, new PrintDocumentAdapter.LayoutResultCallback() {
            @Override
            public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
                printAdapter.onWrite(new PageRange[]{PageRange.ALL_PAGES}, getOutputFile(path, fileName), new CancellationSignal(), new PrintDocumentAdapter.WriteResultCallback() {

                    @Override
                    public void onWriteFinished(PageRange[] pages) {
                        super.onWriteFinished(pages);
                        if (pages.length > 0) {
                            File file = new File(path, fileName);
                            callback.success(file);
                        } else {
                            callback.onFailure();
                        }

                    }
                });
            }
        }, null);

    }


    private ParcelFileDescriptor getOutputFile(File path, String fileName) {
        File file1 = new File(path, fileName);
        if (file1.exists()) {
            file1.delete();
            path.delete();
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, fileName);
        try {
            file.createNewFile();
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e);
        }
        return null;
    }

    private PrintAttributes getDefaultPrintAttrs() {

        return new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, DP_VALUE, DP_VALUE))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

    }

    public interface CallbackPrint {
        void success(File file);

        void onFailure();
    }
}
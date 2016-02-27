package it.jaschke.alexandria;

import com.google.zxing.Result;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by vineet on 23-Jan-16.
 */
public class ScanBook extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getActivity(), "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        ((MainActivity) getActivity()).addBookFromBarcodeScan(rawResult.getText());



//        // Note:
//        // * Wait 2 seconds to resume the preview.
//        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(ScanBook.this);
//            }
//        }, 2000);
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
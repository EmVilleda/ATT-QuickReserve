package quickreserve.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by charlie on 7/10/14.
 */
public class QRScannerActivity extends Activity implements ZXingScannerView.ResultHandler
{
    private String att_uid;
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        att_uid = getIntent().getStringExtra("att_uid");

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("result", rawResult.getText()); // Prints scan results
        Log.v("result", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent intent = new Intent(this, QRResultActivity.class);
        intent.putExtra("value", rawResult.getText());
        intent.putExtra("format", rawResult.getBarcodeFormat().toString());
        intent.putExtra("att_uid", att_uid);
        startActivity(intent);
        finish();
    }

}
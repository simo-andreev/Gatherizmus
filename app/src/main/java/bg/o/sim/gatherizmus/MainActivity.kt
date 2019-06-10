package bg.o.sim.gatherizmus

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val photoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (photoIntent.resolveActivity(packageManager) == null)
                TODO()

            startActivityForResult(photoIntent, REQUEST_TAKE_PHOTO)
        }

        webContent.loadData("Card data will appear here.", "text/plain", "utf-8")
        webContent.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webContent.canGoBack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) TODO()

        val bmp = data?.extras?.get("data") as Bitmap

        val visionImage = FirebaseVisionImage.fromBitmap(bmp)
        val reader = FirebaseVision.getInstance().onDeviceTextRecognizer

        val result = reader.processImage(visionImage)
            .addOnSuccessListener { firebaseVisionText ->
                toast(firebaseVisionText.text)
                // example URL
                // https://gatherer.wizards.com/Pages/Search/Default.aspx?name=+\[Angrath,\]+\[Minotaur\]+\[Pirate\]
                val builder = StringBuilder()
                firebaseVisionText.text.split(" ").forEach {
                    builder.append("+[").append(it).append("]")
                }
                webContent.loadUrl("https://gatherer.wizards.com/Pages/Search/Default.aspx?name=$builder")
            }
            .addOnFailureListener {
                // Task failed with an exception
                toast(it.localizedMessage)
                Log.e("ERR", "Processing failed", it)
            }
    }
}
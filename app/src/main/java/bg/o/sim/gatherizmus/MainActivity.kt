package bg.o.sim.gatherizmus

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            toast("FAILED")
            TODO()
            return
        }


        val bmp = data!!.extras.get("data") as Bitmap


        val visionImage = FirebaseVisionImage.fromBitmap(bmp)
        val reader = FirebaseVision.getInstance().onDeviceTextRecognizer

        val result = reader.processImage(visionImage)
            .addOnSuccessListener { firebaseVisionText ->
                toast(firebaseVisionText.text)
                webContent.loadUrl("https://gatherer.wizards.com/Pages/Search/Default.aspx?name=+[${firebaseVisionText.text}]")
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
                toast(it.localizedMessage)
                Log.e("ERR", "Processing fail", it)
            }

    }

}
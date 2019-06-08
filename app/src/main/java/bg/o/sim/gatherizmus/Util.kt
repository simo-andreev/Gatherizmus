package bg.o.sim.gatherizmus

import android.app.Activity
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT

// EXTENSION FUNCTIONS //

internal fun Activity.toast(text: String, isLong: Boolean = false) {
    Toast.makeText(this, text, if (isLong) LENGTH_LONG else LENGTH_SHORT).show()
}
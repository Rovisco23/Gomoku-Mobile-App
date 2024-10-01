package pt.isel.pdm.project.ui.common.activities

import pt.isel.pdm.project.ui.common.screens.AboutScreen
import pt.isel.pdm.project.ui.common.screens.CreatorInfo
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.project.R

class AboutActivity : ComponentActivity() {

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, AboutActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "AboutActivity.onCreate() called")
        setContent {
            AboutScreen(
                onBackRequested = { finish() },
                onSendEmailRequested = { openSendEmail() },
                authors = authorInfos
            )
        }
    }

    private fun openSendEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, AUTHOR_EMAILS)
                putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT)
            }

            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "AboutActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "AboutActivity.onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "AboutActivity.onDestroy() called")
    }

    private val authorInfos = createAuthorInfos()

    private fun createAuthorInfos(): List<CreatorInfo> {
        val result = mutableListOf<CreatorInfo>()
        for (i in AUTHOR_NAMES.indices) {
            result.add(CreatorInfo(AUTHOR_NAMES[i], AUTHOR_EMAILS[i], R.drawable.author))
        }
        return result
    }
}

val AUTHOR_EMAILS =
    arrayOf("a49508@alunos.isel.pt", "a49487@alunos.isel.pt", "a48337@alunos.isel.pt")
val AUTHOR_NAMES = arrayOf("João Mota", "Ricardo Rovisco", "Daniel Antunes")
private const val EMAIL_SUBJECT = "About the Gomoku App"
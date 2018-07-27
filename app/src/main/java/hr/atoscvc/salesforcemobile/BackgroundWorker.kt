package hr.atoscvc.salesforcemobile

import android.content.Context
import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder


class BackgroundWorker(
        private var context: WeakReference<Context>,
        @Suppress("unused") private var header: String,
        private var response: AsyncResponse,
        private var progressBar: WeakReference<ProgressBar>
) : AsyncTask<String, Void, String>() {

    interface AsyncResponse {
        fun processFinish(output: String)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        (context.get()?.applicationContext as MyApp).cancelTimer()
        progressBar.get()?.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg p0: String): String {
        val type: String = p0[0]
        val loginURL = context.get()?.getString(R.string.loginURL)
        val registerURL = context.get()?.getString(R.string.registerURL)
        val resetPasswordURL = context.get()?.getString(R.string.resetPasswordURL)

        when (type) {
            context.get()?.getString(R.string.typeLogin) -> try {
                val username: String = p0[1]
                val passwordHash: String = p0[2]

                val postData: String = URLEncoder.encode(context.get()?.getString(R.string.username), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(username, context.get()?.getString(R.string.UTF8)) + "&" +
                        URLEncoder.encode(context.get()?.getString(R.string.password), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(passwordHash, context.get()?.getString(R.string.UTF8))

                val url: URL = NetworkUtils.buildUrl(loginURL!!)

                return NetworkUtils.getResponseFromHttpUrl(url, postData)
                        ?: return context.get()?.getString(R.string.serverNoResponse)!!

            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return context.get()?.getString(R.string.malformedURL)!!
            } catch (e: IOException) {
                e.printStackTrace()
                return context.get()?.getString(R.string.exceptionIO)!!
            }

            context.get()?.getString(R.string.typeRegister) -> try {
                val firstName: String = p0[1]
                val lastName: String = p0[2]
                val username: String = p0[3]
                val email: String = p0[4]
                val passwordHash: String = p0[5]

                val postData: String = URLEncoder.encode(context.get()?.getString(R.string.firstName), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(firstName, context.get()?.getString(R.string.UTF8)) + "&" +
                        URLEncoder.encode(context.get()?.getString(R.string.lastName), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(lastName, context.get()?.getString(R.string.UTF8)) + "&" +
                        URLEncoder.encode(context.get()?.getString(R.string.username), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(username, context.get()?.getString(R.string.UTF8)) + "&" +
                        URLEncoder.encode(context.get()?.getString(R.string.email), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(email, context.get()?.getString(R.string.UTF8)) + "&" +
                        URLEncoder.encode(context.get()?.getString(R.string.password), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(passwordHash, context.get()?.getString(R.string.UTF8))

                val url: URL = NetworkUtils.buildUrl(registerURL!!)

                return NetworkUtils.getResponseFromHttpUrl(url, postData)
                        ?: return context.get()?.getString(R.string.serverNoResponse)!!

            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return context.get()?.getString(R.string.malformedURL)!!
            } catch (e: IOException) {
                e.printStackTrace()
                return context.get()?.getString(R.string.exceptionIO)!!
            }

            context.get()?.getString(R.string.typePasswordReset) -> try {
                val username: String = p0[1]
                val email: String = p0[2]

                val postData: String = URLEncoder.encode(context.get()?.getString(R.string.username), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(username, context.get()?.getString(R.string.UTF8)) + "&" +
                        URLEncoder.encode(context.get()?.getString(R.string.email), context.get()?.getString(R.string.UTF8)) + "=" + URLEncoder.encode(email, context.get()?.getString(R.string.UTF8))


                val url: URL = NetworkUtils.buildUrl(resetPasswordURL!!)

                return NetworkUtils.getResponseFromHttpUrl(url, postData)
                        ?: return context.get()?.getString(R.string.serverNoResponse)!!

            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return context.get()?.getString(R.string.malformedURL)!!
            } catch (e: IOException) {
                e.printStackTrace()
                return context.get()?.getString(R.string.exceptionIO)!!
            }
        }
        return context.get()?.getString(R.string.typeNotRecognized)!!
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        progressBar.get()?.visibility = View.INVISIBLE
        response.processFinish(result)
    }
}
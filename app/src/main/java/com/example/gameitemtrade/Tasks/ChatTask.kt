package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

// 안드로이드에서 사용가능한 스레드 구현방법중에 하나
// UI는 메인스레드에서만 이용가능
// doInBackground 작업중 publishProgress( )메소드 이용 UI접근 가능
public class ChatTask : AsyncTask<String, Void, String>(){
    //doInBackground 파라미터 타입, execute 메소드 인자값
    //onProgressUpdate 파라미터 타입
    //doInBackground 리턴값
    var ip = "192.168.55.69:8090" //자신의 IP번호 로컬호스트X
    var sendMsg: String? = null //? null을 허용
    var receiveMsg:String? = null
    var serverip = "http://$ip/connectDB/search.jsp" // 연결할 jsp주소

    init{
         Log.d("test","JspTask init")
     }

    //vararg var 초기화 이후에도 변경가능한 arg 가변길이
    override fun doInBackground(vararg params: String?): String {
        try {
            var str:String?
            val url : URL = URL(serverip)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection // 웹 통해 데이터의 연결을 위해
            //Content-Type 형식으로 전송, response body 전달시 "application/x-www-form-urlencoded로 서버에 전달
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.requestMethod = "POST"//get방식 or post방식
            //연결된 서버의 outputStream으로 문자를 출력시키는 output 스트림연결
            val osw = OutputStreamWriter(conn.outputStream)
            sendMsg = "Seller="+params[0]+"&Buyer="+params[1]+"&Function="+params[2];
            Log.d("test", sendMsg!!)

            osw.write(sendMsg); //문자출력 스트림에 sendMsg을 씀
            osw.flush(); // 스트림버퍼에 내용이 전부 차있지 않더라도 전송
            osw.close();

            if (conn.responseCode === HttpURLConnection.HTTP_OK) {
                val tmp = InputStreamReader(conn.inputStream, "UTF-8")
                val reader = BufferedReader(tmp)
                val buffer = StringBuffer()

                do{
                    str = reader.readLine()
                    buffer.append(str)
                } while(str != null)


                receiveMsg = buffer.toString()
                Log.d("test", receiveMsg!!)

                Log.d("test", "통신완료")

            } else {
                Log.i("통신 결과", conn.responseCode.toString() + "에러")
            }

        }catch (e : MalformedURLException){
            e.printStackTrace()
        }catch (e : IOException){
            e.printStackTrace()
        }
        Log.d("test", receiveMsg!!)
        var temp1 = receiveMsg?.split(("<body>"))
        var temp2 = temp1?.get(1)?.split(("</body>"))
        val real =  temp2?.get(0)?.trim()
        return real!!
    }
/*
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d("test", " <<<<<onPostExecute>>>> ");
        var temp1 = result?.split(("<body>"))
        var temp2 = temp1?.get(1)?.split(("</body>"))
        val real =  temp2?.get(0)?.trim()
    }
 */
}
package castro.cristi.myfeelings_247580

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()
    private lateinit var graphHappy: View
    private lateinit var graphVeryHappy: View
    private lateinit var graphNeutral: View
    private lateinit var graphSad: View
    private lateinit var graphVerySad: View
    private lateinit var graph: View
    private lateinit var icon: ImageView
    private lateinit var guardarButton: Button
    private lateinit var veryHappyButton: ImageButton
    private lateinit var happyButton: ImageButton
    private lateinit var neutralButton: ImageButton
    private lateinit var sadButton: ImageButton
    private lateinit var verySadButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        graphHappy = findViewById(R.id.graphHappy)
        graphVeryHappy = findViewById(R.id.graphVeryHappy)
        graphNeutral = findViewById(R.id.graphNeutral)
        graphSad = findViewById(R.id.graphSad)
        graphVerySad = findViewById(R.id.graphVerySad)
        graph = findViewById(R.id.graph)
        icon = findViewById(R.id.icon)
        guardarButton = findViewById(R.id.guardButton)
        veryHappyButton = findViewById(R.id.veryHappyButton)
        happyButton = findViewById(R.id.happyButton)
        neutralButton = findViewById(R.id.neutralButton)
        sadButton = findViewById(R.id.sadButton)
        verySadButton = findViewById(R.id.verySadButton)

        jsonFile = JSONFile()

        fetchingData()
        if(!data) {
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones )
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones ("Muy feliz", 0.0F, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones ("Feliz", 0.0F, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones ("Neutral",0.0F, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones ("Triste", 0.0F, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones ("Muy triste",0.0F, R.color.deepBlue, verysad))
        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener(){
            guardar()
        }

        veryHappyButton.setOnClickListener(){
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener(){
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener(){
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener(){
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        verySadButton.setOnClickListener(){
            verysad++
            iconoMayoria()
            actualizarGrafica()
        }


    }


    fun fetchingData(){
        try {
            var json: String = jsonFile?.getData(this) ?: ""
            if (json != ""){
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for (i in lista){
                    when (i.nombre){
                        "Muy feliz" -> veryHappy =  i.total
                        "Feliz" -> happy =  i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad =  i.total
                        "Muy triste" -> verysad = i.total
                    }
                }
            } else {
                this.data = false
            }
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
    }

    fun parseJson (jsonArray: JSONArray): ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()

        for (i in 0..jsonArray.length()) {
            try {

                val nombre= jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones (nombre, porcentaje, color, total )
                lista.add(emocion)
            } catch (exception: JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun actualizarGrafica(){

        val total = veryHappy+happy+neutral+verysad+sad
        var pVH: Float = (veryHappy * 100 / total).toFloat()
        var pH: Float = (happy * 100 /total).toFloat()
        var pN: Float = (neutral * 100 /total).toFloat()
        var pS: Float = (sad * 100 / total).toFloat()
        var pVS: Float = (verysad * 100 / total).toFloat()


        Log.d("porcentajes", "very happy "+pVH)
        Log.d( "porcentajes"," happy "+pH)
        Log.d("porcentajes","neutral "+pN)
        Log.d("porcentajes","sad "+pS)
        Log.d("porcentajes","very sad "+pVS)

        lista.clear()
        lista.add(Emociones ( "Muy feliz",pVH, R.color.mustard, veryHappy))
        lista.add(Emociones( "Feliz", pH, R.color.orange, happy))
        lista.add(Emociones ("Neutral",pN, R.color.greenie, neutral))
        lista.add(Emociones ("Triste",pS, R.color.blue,sad))
        lista.add(Emociones ("Muy triste", pVS, R.color.deepBlue, verysad))

        val fondo = CustomCircleDrawable(this, lista)


        graphVeryHappy.background = CustomBarDrawable(this, Emociones ("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones ("Feliz", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones ("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones ("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones ("Muy triste", pVS, R.color.deepBlue, verysad))
        graph.background = fondo
    }

    fun iconoMayoria(){
        if (happy>veryHappy && happy>neutral && happy>sad && happy>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy2))
        }
        if (veryHappy>happy && veryHappy>neutral && veryHappy>sad && veryHappy>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }

        if (neutral>veryHappy && neutral >happy && neutral>sad && neutral>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }

        if (sad>happy && sad>neutral && sad>veryHappy && sad>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }

        if (verysad>happy && verysad>neutral && verysad>sad && veryHappy<verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.iv_verysad))
        }

    }

    fun guardar(){

        var jsonArray = JSONArray()
        var o : Int = 0
        for (i in lista){
            Log.d("objetos", i.toString())
            var j: JSONObject = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)
            jsonArray.put(o, j)
            o++
        }

        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this,"Datos Guardados",Toast.LENGTH_SHORT).show()
    }
}
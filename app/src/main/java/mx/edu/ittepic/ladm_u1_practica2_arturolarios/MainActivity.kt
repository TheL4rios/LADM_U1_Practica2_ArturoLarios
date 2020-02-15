package mx.edu.ittepic.ladm_u1_practica2_arturolarios

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grantPermission()

        btnGuardar.setOnClickListener {
            file(true)
        }

        btnAbrir.setOnClickListener {
            file(false)
        }
    }

    fun file(action : Boolean)
    {
        if(!rdExterna.isChecked && !rdInterna.isChecked)
        {
            message("ATENCION!!", "Debe seleccionar un modo de almacenamiento")
            return
        }

        if(txtArchivo.text.toString().isEmpty())
        {
            message("ATENCION!", "Debe llenar el campo del nombre de archivo")
            return
        }

        if(action)
        {
            saveFile()
        }
        else
        {
            readFile()
        }
    }

    fun saveFile()
    {
        if(rdExterna.isChecked)
        {
            if(!thereisSD())
            {
                message("ERROR!", "No se encontró la memoria externa")
                return
            }

            saveOnSD(txtArchivo.text.toString())
        }
        else
        {
            saveOnInternal(txtArchivo.text.toString())
        }
    }

    fun saveOnSD(file : String)
    {
        try
        {
            var path = Environment.getExternalStorageDirectory()
            var dataFile = File(path.absolutePath, file)
            var output = OutputStreamWriter(FileOutputStream(dataFile))
            var data = txtFrase.text.toString()

            output.write(data)
            output.flush()
            output.close()

            message("ATENCION!", "Se guardó correctamente")
        }
        catch(error : IOException)
        {
            message("ERROR!", error.message.toString())
        }
    }

    fun saveOnInternal(file : String)
    {
        try
        {
            var output = OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE))
            var data = txtFrase.text.toString()

            output.write(data)
            output.flush()
            output.close()

            message("ATENCION!", "Se guardó correctamente")
        }
        catch(error : IOException)
        {
            message("ERROR!", error.message.toString())
        }
    }

    fun readOnSD(file : String)
    {
        try
        {
            var path = Environment.getExternalStorageDirectory()
            var dataFile = File(path.absolutePath, file)

            var input = BufferedReader(InputStreamReader(FileInputStream(dataFile)))

            var data = input.readLine()
            input.close()

            txtFrase.setText(data)
        }
        catch(error : IOException)
        {
            message("ERROR!", error.message.toString())
        }
    }

    fun readOnInternal(file : String)
    {
        try
        {
            var input = BufferedReader(InputStreamReader(openFileInput(file)))

            var data = input.readLine()
            input.close()

            txtFrase.setText(data)
        }
        catch(error : IOException)
        {
            message("ERROR!", error.message.toString())
        }
    }

    fun readFile()
    {
        if(rdExterna.isChecked)
        {
            if(!thereisSD())
            {
                message("ERROR!", "No se encontró la memoria externa")
                return
            }

            readOnSD(txtArchivo.text.toString())
        }
        else
        {
            readOnInternal(txtArchivo.text.toString())
        }
    }

    fun grantPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                            Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    fun thereisSD() : Boolean
    {
        var estado = Environment.getExternalStorageState()

        if(estado != Environment.MEDIA_MOUNTED)
        {
            return false
        }

        return true
    }

    fun message(title : String, text : String)
    {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(text)
            .setPositiveButton("OK"){d, i->}
            .show()
    }
}

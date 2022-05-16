package com.example.todos

import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.todos.databinding.ActivityMainBinding
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var todosRecylerView: RecyclerView
    private lateinit var todosList: ArrayList<Todos>
    private lateinit var adapter: TodosAdapter
    private lateinit var loadingDialog: Dialog

    public var myText:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** initializing the progress dialog  **/
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(R.layout.progress_layout)
        loadingDialog = alertDialog.create()

        /** initializing array list adapter views **/
        todosList = ArrayList()
        adapter = TodosAdapter(this, todosList)
        todosRecylerView = findViewById(R.id.recycler_view)
        todosRecylerView.layoutManager = LinearLayoutManager(this)
        todosRecylerView.adapter = adapter

        jsonParse()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun jsonParse() {
        loadingDialog.show()
        var requestQueue: RequestQueue? = null
        requestQueue = Volley.newRequestQueue(this)
        val url = "https://tarek-todo-app.herokuapp.com/todos"
        val request =
            JsonArrayRequest(Request.Method.GET, url, null, { response ->
                try {
                    val num = response.length()-1
                    for(i in 0..num){
                        //Toast.makeText(this, "\n\n${response.getJSONObject(i)}", Toast.LENGTH_LONG).show()
                        todosList.add(Todos(response.getJSONObject(i)["_id"].toString(),
                        response.getJSONObject(i)["content"].toString(),
                        response.getJSONObject(i)["createdAt"].toString(),
                        response.getJSONObject(i)["isDone"].toString().toBoolean()))

                    }
                adapter.notifyDataSetChanged()
                    loadingDialog.dismiss()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Hi 1\n${e.message.toString()}",Toast.LENGTH_LONG).show()
                    loadingDialog.dismiss()
                }
            }, { error ->
                error.printStackTrace()
                loadingDialog.dismiss()
                Toast.makeText(this, "Hi 2\n${error.message.toString()}",Toast.LENGTH_LONG).show()
            })
        requestQueue?.add(request)
    }

    public fun toggleStatus(id:String?) {
        loadingDialog.show()
        var requestQueue: RequestQueue? = null
        requestQueue = Volley.newRequestQueue(this)
        val url = "https://tarek-todo-app.herokuapp.com/toggle/${id}"
        val request =
            JsonArrayRequest(Request.Method.POST, url, null, { response ->
                try {
                    val num = response.length()-1
                    this.recreate()
                    adapter.notifyDataSetChanged()
                    loadingDialog.dismiss()
                } catch (e: JSONException) {
                    e.printStackTrace()
                   // Toast.makeText(this, "Hi 1\n${e.message.toString()}",Toast.LENGTH_LONG).show()
                    loadingDialog.dismiss()
                   this.recreate()
                }
            }, { error ->
                this.recreate()
                error.printStackTrace()
                loadingDialog.dismiss()
              //  Toast.makeText(this, "Hi 2\n${error.message.toString()}",Toast.LENGTH_LONG).show()
            })
        requestQueue?.add(request)
    }

    public fun showConfirmation(id:String?)
    {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("This Todo item is already marked as completed, do you want to change its status?")
            .setIcon(resources.getDrawable(R.drawable.problem))
            .setPositiveButton("Yes"){
              _,_ -> toggleStatus(id)
            }
            .setNegativeButton("No"){
                    _,_ -> return@setNegativeButton
            }.show()
    }

    public fun newTodoDialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Title")
        val input = EditText(this)
        input.setHint("Enter Text")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var m_Text = input.text.toString()
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

}
package com.example.filterwords.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.filterwords.R
import com.example.filterwords.WordListFragment

class WordAdapter( context: Context, private val letterId: String ):
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private val filteredWords: List<String>

    init {
        val words = context.resources.getStringArray(R.array.words).toList()

        filteredWords = words.filter { it.startsWith(letterId, ignoreCase = true) }
            .shuffled()
            .take(5)
            .sorted()
    }

    class WordViewHolder( val view: View): RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {

        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        // Setup custom accessibility delegate to set the text read
        layout.accessibilityDelegate = Accessibility

        return WordViewHolder(layout)

    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item  =  filteredWords[position]

        // Needed to call startActivity
        val context = holder.view.context

        holder.button.text = item
        holder.button.setOnClickListener {
            val queryUrl: Uri = Uri.parse("${WordListFragment.SEARCH_PREFIX}${item}")
            val intent = Intent( Intent.ACTION_VIEW, queryUrl)
            context.startActivity(intent)
            /** OTROS TIPOS DE INTENTS => https://developer.android.com/guide/components/intents-common
             * ACTION_VIEW => Abre una URI en el navegador web
             * CATEGORY_APP_MAPS => Inicia la app de mapas
             * CATEGORY_APP_GALLERY => Inicia la app de fotos(galeria)
             * ACTION_SET_ALARM => Inicia la app de alarma
             * ACTION_DIAL => Inicia una llamada teléfonica    */

        }
    }

    override fun getItemCount() = filteredWords.size

    // Setup custom accessibility delegate to set the text read with
    // an accessibility service
    companion object Accessibility : View.AccessibilityDelegate() {
        //@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(
            host: View?,
            info: AccessibilityNodeInfo?
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // With `null` as the second argument to [AccessibilityAction], the
            // accessibility service announces "double tap to activate".
            // If a custom string is provided,
            // it announces "double tap to <custom string>".
            val customString = host?.context?.getString(R.string.look_up_word)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info?.addAction(customClick)
        }
    }

}
package com.alexandr.deadlineapp.Presentation.Adapter


import android.R.menu
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.Domain.AddDeadlineViewModel
import com.alexandr.deadlineapp.Domain.DeadlineViewModel
import com.alexandr.deadlineapp.Presentation.Acrivities.Main.MainActivity
import com.alexandr.deadlineapp.Presentation.Item.DeadlinesViewHolder
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.alexandr.deadlineapp.Utils.Utils


class MyDeadlineRecyclerViewAdapter (val items : ArrayList<Deadline>,
                                     val context : Context,
                                     val deadlineViewModel: DeadlineViewModel
) :
    RecyclerView.Adapter<DeadlinesViewHolder>() {

    private var position: Int = -1

    fun getPosition() : Int{
        return position
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlinesViewHolder {
        return DeadlinesViewHolder(LayoutInflater.from(context).inflate(R.layout.deadline_card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun onBindViewHolder(holder: DeadlinesViewHolder, position: Int) {
        holder.predmet?.text = items[position].name.toUpperCase()
        holder.predmet?.typeface = Typeface.DEFAULT_BOLD;
        holder.zadanie?.text = items[position].description
        holder.completed.isChecked = items[position].completed
        if (items[position].pinned){
            holder.pinned.visibility = View.VISIBLE
        }
        holder.datetime.text = items[position].date + ", " + items[position].time
        holder.completed.setOnClickListener {
            (it as RadioButton).isChecked = items[position].completed
        }
        var color: Int = R.color.white
        if (items[position].pinned) {
            color = R.color.lightgrey
        }
        if (items[position].completed) {
            color = R.color.lightgreen
        }
        holder.card.setOnCreateContextMenuListener { menu, _, _ ->
            if (!items[position].completed) {
                menu.add("Выполнено").setOnMenuItemClickListener {
                    items[position].completed = true
                    deadlineViewModel.updateOne(items[position])
                    true
                }
                if (items[position].pinned) {
                    menu.add("Открепить").setOnMenuItemClickListener {
                        Toast.makeText(context, "Дедлайн откреплен", Toast.LENGTH_SHORT).show()
                        items[position].pinned = false
                        deadlineViewModel.updateOne(items[position])
                        true
                    }
                } else {
                    menu.add("Закрепить").setOnMenuItemClickListener {
                        Toast.makeText(context, "Дедлайн закреплен", Toast.LENGTH_SHORT).show()
                        items[position].pinned = true
                        deadlineViewModel.updateOne(items[position])
                        true
                    }
                }
                menu.add("Редактировать").setOnMenuItemClickListener {
                    //addDeadlineViewModel.edit = true
                    true
                }
            }
            menu.add("Удалить").setOnMenuItemClickListener {
                Toast.makeText(context, "Дедлайн удален", Toast.LENGTH_SHORT).show()
                deadlineViewModel.deleteOne(items[position])
                true
            }
        }
        holder.card.setCardBackgroundColor(ContextCompat.getColor(context,color))

    }

}

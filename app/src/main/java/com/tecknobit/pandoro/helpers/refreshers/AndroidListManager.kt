package com.tecknobit.pandoro.helpers.refreshers

import com.google.gson.Gson
import com.tecknobit.pandoro.helpers.ui.ListManager
import com.tecknobit.pandoro.records.structures.PandoroItemStructure

/**
 * The **AndroidListManager** interface is useful to manage a list of items in the UI
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ListManager
 */
interface AndroidListManager: ListManager {

    companion object {

        private val gson = Gson()

    }

    /**
     * Function to check whether the list to display in the UI need to be refreshed due changes
     *
     * @param currentList: the current list displayed
     * @param newList: the new hypothetical list to set and display
     * @param T: the type of the items of the lists
     *
     * @return whether refresh the list as [Boolean]
     */
    override fun <T : PandoroItemStructure> needToRefresh(
        currentList: List<T>,
        newList: List<T>
    ): Boolean {
        return ((currentList.isEmpty() && newList.isNotEmpty()) ||
                (gson.toJson(currentList) != gson.toJson(newList)))
    }

}
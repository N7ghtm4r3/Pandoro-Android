package com.tecknobit.pandoro.helpers.refreshers

import com.google.gson.Gson
import com.tecknobit.pandoro.helpers.ui.SingleItemManager
import com.tecknobit.pandoro.records.structures.PandoroItemStructure

/**
 * The **AndroidSingleItemManager** interface is useful to manage a single item in the UI
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see SingleItemManager
 */
interface AndroidSingleItemManager: SingleItemManager {

    companion object {

        private val gson = Gson()

    }

    /**
     * Function to check whether the item to display in the UI need to be refreshed due changes
     *
     * @param currentItem: the current item displayed
     * @param newItem: the new hypothetical item to set and display
     * @param T: the type of the item
     *
     * @return whether refresh the item as [Boolean]
     */
    override fun <T : PandoroItemStructure> needToRefresh(currentItem: T, newItem: T): Boolean {
        return gson.toJson(currentItem) != gson.toJson(newItem)
    }

}
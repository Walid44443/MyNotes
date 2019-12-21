package codes.walid4444.notes.Helper.FireStore.callback


interface onSaveNote {
    fun onSuccess(fireStoreID : String)
    fun onError(errorMsg : String)

}
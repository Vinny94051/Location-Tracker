package vlnny.base.viewModel

import androidx.lifecycle.*

abstract class BaseViewModel : ViewModel(), LifecycleObserver {
    protected val isProgressVisibleLiveData = MutableLiveData(false)
    val isProgressVisible: LiveData<Boolean>
        get() = isProgressVisibleLiveData
}
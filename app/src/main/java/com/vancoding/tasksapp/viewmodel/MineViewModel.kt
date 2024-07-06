import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import kotlinx.coroutines.launch

class MineViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    fun getUserInfo() {
        viewModelScope.launch {
            val userId = PreferencesManager.getUserId(getApplication()) // Pass application context
            val user = userId?.let { repository.getUserById(it) }
            _userInfo.postValue(UserInfo(user?.nickname ?: "", user?.username ?: ""));
        }
    }

    fun updateNickname(newNickname: String, userId: Int) {
        viewModelScope.launch {
            repository.updateNickname(newNickname, userId);
            getUserInfo(); // Fetch updated user info
        }
    }

    data class UserInfo(
        val nickname: String,
        val username: String
    );
}

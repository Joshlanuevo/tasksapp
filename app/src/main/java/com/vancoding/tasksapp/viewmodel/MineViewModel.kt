import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import kotlinx.coroutines.launch

class MineViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {

    private val TAG = "MineViewModel"

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    init {
        Log.d(TAG, "MineViewModel initialized")
    }

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                val userId = PreferencesManager.getUserId(getApplication())
                Log.d(TAG, "getUserInfo: userId = $userId")
                val user = userId?.let { repository.getUserById(it) }
                _userInfo.postValue(UserInfo(user?.nickname ?: "", user?.username ?: "", user?.avatarUrl))
                Log.d(TAG, "getUserInfo: UserInfo updated - ${_userInfo.value}")
            } catch (e: Exception) {
                Log.e(TAG, "getUserInfo error: ${e.message}", e)
            }
        }
    }

    fun updateNickname(newNickname: String, userId: Int) {
        viewModelScope.launch {
            try {
                repository.updateNickname(newNickname, userId)
                getUserInfo() // Fetch updated user info
                Log.d(TAG, "updateNickname: Updated nickname to $newNickname for userId $userId")
            } catch (e: Exception) {
                Log.e(TAG, "updateNickname error: ${e.message}", e)
            }
        }
    }

    fun updateAvatar(avatarUrl: String) {
        viewModelScope.launch {
            try {
                val userId = PreferencesManager.getUserId(getApplication())
                Log.d(TAG, "updateAvatar: userId = $userId, avatarUrl = $avatarUrl")
                if (userId != null) {
                    repository.updateAvatar(userId, avatarUrl)
                }
                getUserInfo()
                Log.d(TAG, "updateAvatar: Avatar updated to $avatarUrl for userId $userId")
            } catch (e: Exception) {
                Log.e(TAG, "updateAvatar error: ${e.message}", e)
            }
        }
    }

    data class UserInfo(
        val nickname: String,
        val username: String,
        val avatar: String?
    )
}

# AndroidAssignment1
App for archeology students to record list of hill forts they have visited and write notes.
After the splashscreen, the user is prompted to login.


![][login]


Before logging in, the user must register to create an account. Email addresses must be unique.


![][register]


The user is then presented with a list of hillforts if previously added. For new users, this screen will be empty.


![][list]


Upon pressing the plus button, the user is taken to the add hillfort screen. This is quite long and allows the user to do many different things, such as:

Enter a name, description and notes...


![][nameDescriptionNotes]


Enter a map location...


![][map]


Enter a date that the hillfort was visited...


![][datePicker]


Check a box to record that they have visited that hillfort
Upload view and delete up to 4 images...


![][multipleImages]


Going back to the hillforts list screen, the user can also select options. Within the options screen, the user can:
Change their email and password...


![][settings1]


View the total number of hillforts recorded for all users, the number of hillforts recorded by the user only and the number of hillforts that the user has marked as visited...


![][settings2]


And finally, from the main screen, the user can logout...


![][list]


The user model looks like...
```
@Parcelize
data class HillfortModel(var id: Long = 0,
                         var name: String = "",
                         var description: String = "",
                         var notes: String = "",
                         var images: ArrayList<String> = arrayListOf(),
                         var lat: Double = 0.0,
                         var lng: Double = 0.0,
                         var zoom: Float = 0f,
                         var visitedDate: String = "",
                         var authorId: Long = 0,
                         var visited: Boolean = false ) : Parcelable
```
The Hillfort model looks like...
```
@Parcelize
data class UserModel(var id: Long = 0,
                var firstName: String = "",
                var lastName: String = "",
                var email: String = "",
                var password: String = "") : Parcelable
```

All features in the grading rubrick have been implemented except for the unified json and navigation drawers.
The up button is not functioning correctly. It returns users to the log in screen rather than the parent activity.

[datePicker]: ./readme/datePicker.png
[login]: ./readme/login.png
[map]: ./readme/map.png
[multipleImages]: ./readme/multipleImages.png
[nameDescriptionNotes]: ./readme/nameDescriptionNotes.png
[register]: ./readme/register.png
[list]: ./readme/list.png
[settings1]: ./readme/settings1.png
[settings2]: ./readme/settings2.png


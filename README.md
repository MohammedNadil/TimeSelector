# A simple time second selector seek bar custom made

![Imgur Image](https://github.com/MohammedNadil/TimeSelector/blob/master/seekimg.jpg) 


How to To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
		implementation 'com.github.User:Repo:1.2'
	}

Can customize following colors and max time

    <com.timeselect.timeselector.TimerPickView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:addBorderColor="@color/purple_500"
        app:addtextColor="@color/black"
        app:addbodyColor="@color/black"
        app:addEndColor="@color/purple_500"
        app:addStartColor="@color/purple_500"
        app:addlineColor="@color/purple_500"
        app:addselectColor="@color/white"
        app:addEndTime="50"/>
	
Can add listener as below

	 view.setTimeChangeListener(object : TimeChanger {
            override fun timeChanged(it: Int) {
               
            }
        })

Can reset and set max time programmatically

	view.reset()
       
        view.setMax(15)

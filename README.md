# Check-Password-Strength
A simple UI for checking a password strength

https://www.youtube.com/watch?v=vlzFNnvDGR0

The lib supports 5 states of password:
- None (empty string)
- Very weak
- Weak 
- Strong
- Perfect


As a default behavior the strength is based on the length of the password, but you can set your own rules.

## To implement it in your app you have to do following steps:
1. Copy and paste "password_checker" package into your app
2. Copy a region "PasswordCheckView" into your /values/attrs.xml
3. Add a PasswordCheckView into your xml layout and configure it.
4. Attach a TextWatcher listener to the view in your Activity / Fragment


## Configuring the view
Use following View attributes to configure a View in the xml layout

<code> 
        
        app:pass_check_anim_interpolator="AccelerateDecelerateInterpolator"
        
        app:pass_check_animation_duration="400"
        
        app:pass_check_color_reasonable="#03A9F4"
        
        app:pass_check_color_strong="#4CAF50"
        
        app:pass_check_color_weak="@color/purple_700"
        
        app:pass_check_indicator_height="4dp"
        
        app:pass_check_indicator_marginTop="4dp"
        
        app:pass_check_indicator_text_gravity="center"
        
        app:pass_check_indicator_text_size="14dp"
        
        app:pass_check_name_reasonable="Good"
        
        app:pass_check_name_strong="Perfectos"
        
        app:pass_check_name_very_weak="Very weak"
        
        app:pass_check_name_weak="@string/app_name"
  </code>
  
  ### Why it's not a library that I can add as a dependency in the build.gradle?
  It's pretty small code and implementing it as a code you can change / extend it as you wish

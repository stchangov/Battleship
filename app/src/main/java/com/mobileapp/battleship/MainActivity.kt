/***********************************************
Authors: Luca Azevedo, Sean Tong, Stephan Tchangov
Date: 12/11/2025
Purpose: Our project is to build a turn-based, pass-and-play Battleship app
that allows two players to set up boards; exchange turns and complete a full
game using a single device. The mission is to create a simple, accessible, and
engaging mobile experience that brings the classic strategy game to Android without
requiring accounts or online connectivity.
What Learned:
    Android Fragment Architecture
    Navigation Graph & ViewBinding
    Team Git Workflow
    UI/UX Design Consistency
    Android Animations
    Data & File Saving
Sources of Help:
    Blackboard Class Learning Materials
    https://kotlinlang.org/docs/home.html
    https://developer.android.com/reference
Time Spent (Hours): 62 hours
 ***********************************************/

/*
Mobile App Development I -- COMP.4630 Honor Statement
The practice of good ethical behavior is essential for maintaining good order
in the classroom, providing an enriching learning experience for students,
and training as a practicing computing professional upon graduation. This
practice is manifested in the University's Academic Integrity policy.
Students are expected to strictly avoid academic dishonesty and adhere to the
Academic Integrity policy as outlined in the course catalog. Violations will
be dealt with as outlined therein. All programming assignments in this class
are to be done by the student alone unless otherwise specified. No outside
help is permitted except the instructor and approved tutors.
I
certify that the work submitted with this assignment is mine and was
generated in a manner consistent with this document, the course academic
policy on the course website on Blackboard, and the UMass Lowell academic
code.
Date: 12/11/2025
Names: Luca Azevedo, Sean Tong, Stephan Tchangov
*/

package com.mobileapp.battleship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobileapp.battleship.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate main activity layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

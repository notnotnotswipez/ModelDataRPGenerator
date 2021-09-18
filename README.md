# ModelDataRPGenerator
Ever wanted to make a plugin with those SICK custom model data items but you we're simply too small brain to figure it out? Well dont worry! This program is for you!

# Setup
1) Download this program and make sure to put it in its own empty folder.
2) Make a .bat file to run the program, run text is below.

```
java -jar ModelDataRPGenerator.jar
```
3) Make sure to run the program one time for it to generate all its necessary folders, after that you can get to actually using it.

# How To Use
1) Put your item model .json and your texture for this model into the *input* folder.

These need to be named correctly so the program can understand what you want to do.
The naming convention is as follows:
```
(ITEM YOU WANT THE MODEL DATA FOR)_(MODEL DATA ID).(.json/.png)
```
Example:
diamond_sword_1.json,
diamond_sword_1.png

2) After that, just run the program with that bat file you created. Your model data pack will be generated in the output folder, the application will tell you what folder it exported to
but its quite easy to tell, the numbers on the folder name should make it pretty obvious.

Yes, thats it. No headaches over what goes where, what does what, what predicates or whatever the heck those are, just two files, and one program.

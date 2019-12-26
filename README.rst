The Nine Puzzle (9P)
===================


.. image:: https://travis-ci.org/tube42/9P.svg
    :target: https://travis-ci.org/tube42/9P


.. raw:: html
   
   <a href="https://f-droid.org/app/se.tube42.p9.android"><img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="100"></a>


This is a FOSS puzzle game for Android.

.. image:: extra/doc/gameplay.gif


Assets
------

Art and design aren't really my strongest side.
Thankfully, this is an open source project and you can help if you want ;)

In any case, while the compiled assets are found under android/assets, the raw assets are include under the extra folder.
To compile assets you will need the following tools:

1. ImageMagic for converting PNG images
2. Inkscape for rendering SVG files

That is,

.. code:: shell

    sudo apt install imagemagick inkscape


To build the assets, you should do

.. code:: shell

    make setup
    make

Valid words
----------

The word-list this game uses is `open source <https://github.com/tube42/wordlists>`_.
File an issue with them if you want to remove or add some words or improve something else.

If you instead have an issue with how 9P works with words, open an issue in the 9P repository.


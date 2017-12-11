The Nine Puzzle (9P)
===================

.. image:: https://travis-ci.org/tube42/9P.svg
    :target: https://travis-ci.org/tube42/9P

This is a FOSS puzzle game for Android. 

.. image:: http://tube42.github.io/9P/img/img00.png


Assets
------

Raw assets are found under the extra folder.
To compile assets you will need the following tools:

1. "sox" for converting samples
2. ImageMagic for converting PNG images
3. Inkscape for rendering SVG files

That is,

.. code:: shell

    sudo apt-get install sox imagemagick inkscape

    
To build the assets, you should do

.. code:: shell

    make setup
    make


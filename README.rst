The Nine Puzzle (9P)
===================

This is a FOSS puzzle game for Android. 

.. image:: http://tube42.github.io/9P/img/img00.png


Building
--------

To build this app, you will need

1. java, ant, android SDK and all that for CODING
2. ImageMagic, Inkscape, vox for ASSETS
3. The rest (libgdx, marm, tweeny, ks, ...) are downloaded when you do setup

To setup the project and download required libraries, binaries (all FOSS) and assets

.. code:: shell

    ant setup

To build the project and run on desktop

.. code:: shell

    ant run

To build for android and upload it to your device

.. code:: shell

    ant debug install

Assets
------

Asset sources are found under the extra folder. To compile assets you will need the following tools:

1. "sox" for converting samples
2. ImageMagic for converting PNG images
3. Inkscape for rendering SVG files

That is,

.. code:: shell

    sudo apt-get install sox imagemagick inkscape

    
To build the assets, you should do

.. code:: shell

    make

Note that "ant setup" will overwrite your generated assets.

INKSCAPE=inkscape

MARM = java -jar submodules/marm/marm_app.jar hiero=libs/bin/hiero -v

##

all: icons marm


icons:
	cd android/res/ && mkdir -p drawable-ldpi drawable-mdpi drawable-hdpi drawable-xhdpi drawable-xxhdpi drawable-xxxhdpi
	$(INKSCAPE) -z extra/icon.svg  -w 36 -h 36 -e android/res/drawable-ldpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 48 -h 48 -e android/res/drawable-mdpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 72 -h 72 -e android/res/drawable-hdpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 96 -h 96 -e android/res/drawable-xhdpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 144 -h 144 -e android/res/drawable-xxhdpi/ic_launcher.png
	$(INKSCAPE) -z extra/icon.svg  -w 192 -h 192 -e android/res/drawable-xxxhdpi/ic_launcher.png

marm:
	-cd android/assets && rm -rf 1 2 4
	$(MARM) resize extra/assets android/assets

##

clean:
	rm -rf android/assets/1
	rm -rf $(CLEAN_ADD)
	rm -rf android/res/drawable-*/ic_launcher.png
	- cd submodules/marm && ant clean

setup:
	cd submodules/marm && ant dist
var World =
{
    loaded: false,

    init: function initFn()
	{
        /*
		 * Disable all sensors in "IR-only" Worlds to save performance. If the
		 * property is set to true, any geo-related components (such as
		 * GeoObjects and ActionRanges) are active. If the property is set to
		 * false, any geo-related components will not be visible on the screen,
		 * and triggers will not fire.
		 */
        AR.context.services.sensors = false;
        this.createOverlays();
    },

    createOverlays: function createOverlaysFn()
    {
        // Initialize Tracker
        // Important: If you replace the tracker file with your own, make sure
		// to change the target name accordingly.
        // e.g. replace "pageOne" used for creating the AR.Trackeable2DOBject
		// below, with the name of one of your new target images.
        this.tracker = new AR.Tracker("wtc/KaptarTargets.wtc");

        // Create overlay for page one
        var wilson = new AR.ImageResource("augmentation/PrinceOfPersia/a_wilson.jpg");
        var overlayOne = new AR.ImageDrawable(wilson, 1, {
            offsetX: 0.05596673976608186,
            offsetY: 0.35694291483113076,
            scale: 0.5
        });

        var pop = new AR.Trackable2DObject(this.tracker, "PrinceofPersia", {
            drawables: {
                cam: [overlayOne]
            },
        });
        
        var cdp = new AR.Trackable2DObject(this.tracker, "cdp-meilleursrecettes", {
            drawables: {
                cam: [overlayOne]
            },
        });
        
        var cjr = new AR.Trackable2DObject(this.tracker, "CerealesJustRight", {
            drawables: {
                cam: [overlayOne]
            },
        });

    },

};

World.init();
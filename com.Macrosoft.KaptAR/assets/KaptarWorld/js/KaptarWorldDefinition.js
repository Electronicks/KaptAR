var World =
{
	loaded: false,

	launch: function launchFn()
	{
		AR.context.services.sensors = false;

		this.tracker = new AR.Tracker("wtc/KaptarTargets.wtc");

		var image159387 = new AR.ImageResource("augmentation/PrinceOfPersia/a_wilson.jpg");
		var a_wilson = new AR.ImageDrawable(image159387, 1, {
			offsetX: -0.7,
			offsetY: 0.35694292
		});

		var hasAnyoneEver = new AR.Label("9.99 $", 0.3, {
			scale: 1,
			offsetX: 0.0,
			offsetY:  -1.0,
			style: {textColor: "#000000",
				backgroundColor: "#FFFFFF"}
		});

		var PrinceofPersia = new AR.Trackable2DObject(this.tracker, "PrinceofPersia", {
			drawables: {
				cam: [a_wilson, hasAnyoneEver]
			},
		});

	}
};

World.launch();

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
		
        var descriptionLabel = new AR.Label(description, 1, {
           offsetX: 0,
           offsetY: 0,
           style: {textColor: "#FFFFFF",
                       backgroundColor: "#000000"}
        });

		var PrinceofPersia = new AR.Trackable2DObject(this.tracker, "PrinceofPersia", {
			drawables: {
				cam: [a_wilson]
			},
		});

	}
};

World.launch();

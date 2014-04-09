var World =
{
	loaded: false,

	launch: function launchFn()
	{
		AR.context.services.sensors = false;

		this.tracker = new AR.Tracker("wtc/KaptarTargets.wtc");

		var image159387 = new AR.ImageResource("kaptarFiles/ubisoft-mtl.png");
		var ubisoft_mtl = new AR.ImageDrawable(image159387, 1, {
			offsetX: -0.7,
			offsetY: 0.0
		});

		var Gameplay_Trailer = new AR.VideoDrawable("kaptarFiles/Gameplay_Trailer.mp4", 1, {
			offsetX: 0.0,
			offsetY: -1.0,
			onClick: function Gameplay_TrailerOnClickFn () {
				Gameplay_Trailer.play(-1);
			}
		});

		var pop_price = new AR.Label("9.99 $", 0.3, {
			scale: 1,
			offsetX: 0.7,
			offsetY:  0.0,
			style: {textColor: "#000000",
				backgroundColor: "#FFFFFF"}
		});

		var PrinceofPersia = new AR.Trackable2DObject(this.tracker, "PrinceofPersia", {
			drawables: {
				cam: [ubisoft_mtl, Gameplay_Trailer, pop_price]
			},
			onEnterFieldOfVision: function onEnterFieldOfViewFn () {
					Gameplay_Trailer.resume();
			},
			onExitFieldOfVision: function onExitFieldOfViewFn () {
				Gameplay_Trailer.pause();
			}
		});

	}
};

World.launch();

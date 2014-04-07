var World =
{
	loaded: false,

	launch: function launchFn()
	{
		AR.context.services.sensors = false;

		this.tracker = new AR.Tracker("wtc/KaptarTargets.wtc");

		var image159387 = new AR.ImageResource("augmentation/ubisoft-mtl.jpeg");
		var ubisoft_mtl = new AR.ImageDrawable(image159387, 1, {
			offsetX: -0.7,
			offsetY: 0.35694291483113076,
			scale: 0.5
		});

		var Gameplay_Trailer = new AR.VideoDrawable("augmentation/Gameplay_Trailer.mp4", 1, {
			offsetX: 0,
			offsetY: -0.4,
			scale: 0.8,
			onClick: function Gameplay_TrailerOnClickFn () {
				Gameplay_Trailer.play(-1);
			}
		});

		var PrinceofPersia = new AR.Trackable2DObject(this.tracker, "PrinceofPersia", {
			drawables: {
				cam: [ubisoft_mtl, Gameplay_Trailer]
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

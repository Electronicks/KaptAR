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

		var image159623 = new AR.ImageResource("kaptarFiles/award.jpg");
		var award = new AR.ImageDrawable(image159623, 1, {
			offsetX: 0.0,
			offsetY: 1.0
		});

		var justright_price = new AR.Label("7.67 $", 0.3, {
			scale: 1,
			offsetX: 0.0,
			offsetY:  -1.0,
			style: {textColor: "#000000",
				backgroundColor: "#FFFFFF"}
		});

		var image12589 = new AR.ImageResource("kaptarFiles/jr_nutriment.jpg");
		var jr_nutriment = new AR.ImageDrawable(image12589, 1, {
			offsetX: 0.7,
			offsetY: 0.0
		});

		var CerealesJustRight = new AR.Trackable2DObject(this.tracker, "CerealesJustRight", {
			drawables: {
				cam: [award, justright_price, jr_nutriment]
			},
		});

		var image513789 = new AR.ImageResource("kaptarFiles/pizza-tropicale.jpg");
		var pizza_tropicale = new AR.ImageDrawable(image513789, 1, {
			offsetX: -0.6,
			offsetY: 0.0
		});

		var cdpexpress_price = new AR.Label("19.99 $", 0.3, {
			scale: 1,
			offsetX: 0.0,
			offsetY:  0.7,
			style: {textColor: "#000000",
				backgroundColor: "#FFFFFF"}
		});

		var image462014 = new AR.ImageResource("kaptarFiles/pizza-recette.jpg");
		var pizza_recette = new AR.ImageDrawable(image462014, 1, {
			offsetX: 0.6,
			offsetY: 0.0
		});

		var cdp_meilleursrecettes = new AR.Trackable2DObject(this.tracker, "cdp-meilleursrecettes", {
			drawables: {
				cam: [pizza_tropicale, cdpexpress_price, pizza_recette]
			},
		});

	}
};

World.launch();

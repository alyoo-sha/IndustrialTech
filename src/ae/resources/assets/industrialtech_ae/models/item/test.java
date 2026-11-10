// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class test<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "test"), "main");
	private final ModelPart front;
	private final ModelPart front_base;
	private final ModelPart front_pick;
	private final ModelPart frame_cube;
	private final ModelPart bottom_cube;
	private final ModelPart top_cube;
	private final ModelPart side_walls;
	private final ModelPart side_left;
	private final ModelPart side;
	private final ModelPart side_pick;
	private final ModelPart side_right;
	private final ModelPart side2;
	private final ModelPart side_pick2;
	private final ModelPart back;
	private final ModelPart back2;
	private final ModelPart back_pick;
	private final ModelPart top;
	private final ModelPart top2;
	private final ModelPart top_pick;
	private final ModelPart bottom;
	private final ModelPart bottom2;
	private final ModelPart bottom_pick;

	public test(ModelPart root) {
		this.front = root.getChild("front");
		this.front_base = this.front.getChild("front_base");
		this.front_pick = this.front.getChild("front_pick");
		this.frame_cube = root.getChild("frame_cube");
		this.bottom_cube = this.frame_cube.getChild("bottom_cube");
		this.top_cube = this.frame_cube.getChild("top_cube");
		this.side_walls = this.frame_cube.getChild("side_walls");
		this.side_left = root.getChild("side_left");
		this.side = this.side_left.getChild("side");
		this.side_pick = this.side_left.getChild("side_pick");
		this.side_right = root.getChild("side_right");
		this.side2 = this.side_right.getChild("side2");
		this.side_pick2 = this.side_right.getChild("side_pick2");
		this.back = root.getChild("back");
		this.back2 = this.back.getChild("back2");
		this.back_pick = this.back.getChild("back_pick");
		this.top = root.getChild("top");
		this.top2 = this.top.getChild("top2");
		this.top_pick = this.top.getChild("top_pick");
		this.bottom = root.getChild("bottom");
		this.bottom2 = this.bottom.getChild("bottom2");
		this.bottom_pick = this.bottom.getChild("bottom_pick");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition front = partdefinition.addOrReplaceChild("front", CubeListBuilder.create(), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition front_base = front.addOrReplaceChild("front_base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 2.0F, -8.0F, 14.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(6.0F, -7.0F, -8.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -7.0F, -8.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -7.0F, -8.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -7.0F, -8.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -7.0F, -8.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -7.0F, -8.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, 0.0F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -7.0F, -8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, -7.0F, -8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -7.0F, -8.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, 1.0F, -8.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, 1.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front_pick = front.addOrReplaceChild("front_pick", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 1.0F, -7.8F, 5.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, 0.0F, -7.8F, 10.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -1.0F, -7.8F, 12.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -2.0F, -7.8F, 12.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -3.0F, -7.8F, 10.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -4.0F, -7.8F, 10.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -5.0F, -7.8F, 8.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -6.0F, -7.8F, 4.0F, 1.0F, 0.8F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame_cube = partdefinition.addOrReplaceChild("frame_cube", CubeListBuilder.create(), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition bottom_cube = frame_cube.addOrReplaceChild("bottom_cube", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 7.0F, -8.0F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 7.0F, 7.0F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 7.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 7.0F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition top_cube = frame_cube.addOrReplaceChild("top_cube", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -8.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -8.0F, -8.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -8.0F, 7.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side_walls = frame_cube.addOrReplaceChild("side_walls", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -14.0F, 0.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -14.0F, 0.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -14.0F, 15.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -14.0F, 15.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 7.0F, -8.0F));

		PartDefinition side_left = partdefinition.addOrReplaceChild("side_left", CubeListBuilder.create(), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition side = side_left.addOrReplaceChild("side", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 5.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -7.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -5.0F, -7.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -5.0F, 5.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -5.0F, 4.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -5.0F, -5.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -4.0F, -3.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -4.0F, 2.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -4.0F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -4.0F, 0.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -4.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 1.0F, 3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -4.0F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -1.0F, -4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 3.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, 4.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side_pick = side_left.addOrReplaceChild("side_pick", CubeListBuilder.create().texOffs(0, 0).addBox(-7.8F, -5.0F, -4.0F, 0.8F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, -4.0F, -4.0F, 0.8F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, -4.0F, 3.0F, 0.8F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, -2.0F, -2.0F, 0.8F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, -1.0F, 1.0F, 0.8F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, 3.0F, 1.0F, 0.8F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, 2.0F, 4.0F, 0.8F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, 3.0F, -4.0F, 0.8F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.8F, 2.0F, -5.0F, 0.8F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side_right = partdefinition.addOrReplaceChild("side_right", CubeListBuilder.create(), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition side2 = side_right.addOrReplaceChild("side2", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, 5.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -7.0F, -7.0F, 1.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -5.0F, -7.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -5.0F, 5.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -5.0F, 4.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -5.0F, -5.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, -3.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, 2.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, 0.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -1.0F, -4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 1.0F, 3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, 1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 3.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 4.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition side_pick2 = side_right.addOrReplaceChild("side_pick2", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -5.0F, -4.0F, 0.8F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, -4.0F, 0.8F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -4.0F, 3.0F, 0.8F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -2.0F, -2.0F, 0.8F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, -1.0F, 1.0F, 0.8F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 3.0F, 1.0F, 0.8F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 2.0F, 4.0F, 0.8F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 3.0F, -4.0F, 0.8F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(7.0F, 2.0F, -5.0F, 0.8F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition back = partdefinition.addOrReplaceChild("back", CubeListBuilder.create(), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition back2 = back.addOrReplaceChild("back2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 5.0F, 7.0F, 12.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -5.0F, 7.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -7.0F, 7.0F, 12.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -7.0F, 7.0F, 2.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -3.0F, 7.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -3.0F, 7.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -3.0F, 7.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -3.0F, 7.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -3.0F, 7.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, -3.0F, 7.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, 4.0F, 7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 4.0F, 7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, 4.0F, 7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -5.0F, 7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, -5.0F, 7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -5.0F, 7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -4.0F, 7.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 3.0F, 7.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition back_pick = back.addOrReplaceChild("back_pick", CubeListBuilder.create().texOffs(0, 0).addBox(4.0F, -5.0F, 7.0F, 1.0F, 2.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -5.0F, 7.0F, 1.0F, 2.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, 3.0F, 7.0F, 1.0F, 2.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0F, -5.0F, 7.0F, 1.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -5.0F, 7.0F, 1.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, 4.0F, 7.0F, 1.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0F, 4.0F, 7.0F, 1.0F, 1.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0F, -3.0F, 7.0F, 1.0F, 6.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -3.0F, 7.0F, 1.0F, 6.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, 3.0F, 7.0F, 1.0F, 2.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -4.0F, 7.0F, 1.0F, 8.0F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -4.0F, 7.0F, 1.0F, 8.0F, 0.8F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition top2 = top.addOrReplaceChild("top2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -7.0F, 11.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -8.0F, 4.0F, 11.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.0F, -8.0F, -7.0F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(4.0F, -8.0F, -4.0F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -8.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -8.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -8.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -8.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition top_pick = top.addOrReplaceChild("top_pick", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.8F, -4.0F, 3.0F, 0.8F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0F, -7.8F, -4.0F, 3.0F, 0.8F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0F, -7.8F, 1.0F, 3.0F, 0.8F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -7.8F, 1.0F, 3.0F, 0.8F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.0F, -7.8F, -1.0F, 2.0F, 0.8F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -7.8F, -1.0F, 2.0F, 0.8F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -7.8F, -3.0F, 2.0F, 0.8F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offset(-1.0F, 24.0F, 1.0F));

		PartDefinition bottom2 = bottom.addOrReplaceChild("bottom2", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -1.0F, 0.0F, 11.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -1.0F, 3.0F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-14.0F, -1.0F, 0.0F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-14.0F, -1.0F, 11.0F, 11.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -1.0F, 4.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -1.0F, 9.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -1.0F, 5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -1.0F, 4.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottom_pick = bottom.addOrReplaceChild("bottom_pick", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -1.0F, 4.0F, 1.0F, 0.8F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -1.0F, 3.0F, 1.0F, 0.8F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -1.0F, 10.0F, 7.0F, 0.8F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-11.0F, -1.0F, 3.0F, 7.0F, 0.8F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -1.0F, 6.0F, 2.0F, 0.8F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -1.0F, 6.0F, 1.0F, 0.8F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -1.0F, 8.0F, 3.0F, 0.8F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -1.0F, 5.0F, 3.0F, 0.8F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -1.0F, 5.0F, 1.0F, 0.8F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		front.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		frame_cube.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		side_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		side_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		back.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		top.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
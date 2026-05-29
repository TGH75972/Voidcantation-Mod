<h1>VoidCantation</h1>
<h3>This mod A highly optimized Fabric mod for Minecraft 1.21 that introduces utility tools designed to manipulate space, coordinates, and storage using clean, state-driven 2D visuals and custom Data Components.</h3>

<b>Features-

<b>1. Architect's Compass
Coordinate Bookmarking: Save specific locations and structures in the world into the compass data.
Distance Tracking: Dynamically tracks and displays your exact distance from the bookmarked position.
Visual Overlays: Renders End Portal style particle lines pointing towards your bookmarked structures.

<b>2. Void Pocket
Singular Deep Storage: A pocket-dimension utility item designed to hold and compress massive quantities of a single block or item type.
Dynamic Texture States: Seamlessly transitions between empty (void_pocket_empty.png) and filled (void_pocket_full.png) states depending on the item data component storage.
Ultra-Flat 2D Hand Models: Scaled precisely on the Z-axis to maintain a clean, flat 2D sprite profile in both first-person and third-person perspectives, eliminating blocky 3D voxel extrusion bugs.

<b>3. Phase Dagger
Spatial Teleportation: Swap physical coordinates instantly with any target entity during combat.
Combat Utility: Highly responsive spatial positioning tool tailored for fluid positioning and evasion.
Technical Specifications & Stack
Platform: Fabric Mod Loader
Minecraft Version: 1.21
Fabric API: 0.102.0+1.21
Fabric Loom: 1.16.2
Java Version: 21 (JDK 21)

Storage Logic: Implemented using Minecraft's native DataComponentType registry API to maintain serialized entity, position, and items data directly inside the item stacks without performance overhead.

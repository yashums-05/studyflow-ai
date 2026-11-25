import math
from PIL import Image, ImageDraw, ImageFilter

def create_studyflow_icon(size):
    # Create high-res canvas (4x supersampling for ultra smooth edges)
    scale = 4
    canvas_size = size * scale
    img = Image.new('RGBA', (canvas_size, canvas_size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    pad = int(canvas_size * 0.08)
    corner_radius = int(canvas_size * 0.22)

    # 1. Background Rounded Squircle with Gradient
    bg = Image.new('RGBA', (canvas_size, canvas_size), (0, 0, 0, 0))
    bg_draw = ImageDraw.Draw(bg)
    bg_draw.rounded_rectangle(
        [pad, pad, canvas_size - pad, canvas_size - pad],
        radius=corner_radius,
        fill=(100, 65, 215, 255) # Base Violet
    )

    # Gradient overlay: from deep violet (#5B21B6) to electric lavender (#A78BFA)
    grad = Image.new('RGBA', (canvas_size, canvas_size), (0, 0, 0, 0))
    grad_draw = ImageDraw.Draw(grad)
    for y in range(canvas_size):
        ratio = y / canvas_size
        r = int(91 * (1 - ratio) + 167 * ratio)
        g = int(33 * (1 - ratio) + 139 * ratio)
        b = int(182 * (1 - ratio) + 250 * ratio)
        grad_draw.line([(0, y), (canvas_size, y)], fill=(r, g, b, 255))
    
    # Mask gradient with squircle
    icon = Image.composite(grad, Image.new('RGBA', (canvas_size, canvas_size), (0, 0, 0, 0)), bg)
    draw = ImageDraw.Draw(icon)

    # Subtle inner border / highlight
    draw.rounded_rectangle(
        [pad + 2, pad + 2, canvas_size - pad - 2, canvas_size - pad - 2],
        radius=corner_radius - 2,
        outline=(255, 255, 255, 60),
        width=int(2 * scale)
    )

    cx = canvas_size / 2
    cy = canvas_size / 2

    # 2. Draw Graduation Cap (Academic Symbol) at upper half
    # Mortarboard Diamond
    cap_cy = cy - canvas_size * 0.06
    w = canvas_size * 0.32
    h = canvas_size * 0.16
    
    diamond = [
        (cx, cap_cy - h),          # Top
        (cx + w, cap_cy),          # Right
        (cx, cap_cy + h),          # Bottom
        (cx - w, cap_cy)           # Left
    ]
    # Drop shadow for cap
    shadow = [(x, y + 4 * scale) for x, y in diamond]
    draw.polygon(shadow, fill=(30, 15, 70, 90))
    # Cap body
    draw.polygon(diamond, fill=(255, 255, 255, 255))
    
    # Cap under-dome (skullcap)
    draw.chord(
        [cx - w * 0.55, cap_cy + h * 0.1, cx + w * 0.55, cap_cy + h * 1.5],
        start=0, end=180,
        fill=(230, 225, 255, 240)
    )

    # Tassel
    tassel_color = (255, 215, 0, 255) # Golden yellow accent
    draw.line([(cx, cap_cy), (cx + w * 0.85, cap_cy + h * 0.4)], fill=tassel_color, width=int(2.5 * scale))
    draw.ellipse([cx + w * 0.82, cap_cy + h * 0.36, cx + w * 0.94, cap_cy + h * 0.48], fill=tassel_color)
    draw.line([(cx + w * 0.88, cap_cy + h * 0.44), (cx + w * 0.88, cap_cy + h * 1.1)], fill=tassel_color, width=int(3 * scale))

    # Center button
    draw.ellipse([cx - 4 * scale, cap_cy - 4 * scale, cx + 4 * scale, cap_cy + 4 * scale], fill=(220, 220, 240, 255))

    # 3. Draw Smart AI Brain / Flow Waves at bottom half
    flow_cy = cy + canvas_size * 0.22
    flow_w = canvas_size * 0.28
    
    # Concentric AI glowing nodes and interconnecting neural flow lines
    glow_color = (255, 255, 255, 220)
    node_color = (255, 255, 255, 255)
    accent_node = (255, 215, 0, 255)

    # Arc 1: Learning Flow Wave
    draw.arc(
        [cx - flow_w, flow_cy - flow_w * 0.5, cx + flow_w, flow_cy + flow_w * 0.5],
        start=200, end=340,
        fill=glow_color,
        width=int(3.5 * scale)
    )
    # Arc 2: Lower Flow Wave
    draw.arc(
        [cx - flow_w * 0.7, flow_cy - flow_w * 0.2, cx + flow_w * 0.7, flow_cy + flow_w * 0.7],
        start=190, end=350,
        fill=(220, 210, 255, 200),
        width=int(2.5 * scale)
    )

    # Neural Synapse Nodes
    nodes = [
        (cx - flow_w * 0.8, flow_cy - flow_w * 0.15, 5 * scale, node_color),
        (cx - flow_w * 0.3, flow_cy - flow_w * 0.45, 6 * scale, node_color),
        (cx, flow_cy - flow_w * 0.35, 7 * scale, accent_node), # Center golden pulse
        (cx + flow_w * 0.3, flow_cy - flow_w * 0.45, 6 * scale, node_color),
        (cx + flow_w * 0.8, flow_cy - flow_w * 0.15, 5 * scale, node_color),
        (cx - flow_w * 0.5, flow_cy + flow_w * 0.1, 4 * scale, (230, 220, 255, 240)),
        (cx + flow_w * 0.5, flow_cy + flow_w * 0.1, 4 * scale, (230, 220, 255, 240)),
        (cx, flow_cy + flow_w * 0.25, 5 * scale, node_color),
    ]

    for x, y, r, c in nodes:
        draw.ellipse([x - r, y - r, x + r, y + r], fill=c)

    # Interconnecting thin neural lines
    connections = [
        (0, 1), (1, 2), (2, 3), (3, 4),
        (0, 5), (5, 7), (7, 6), (6, 4),
        (2, 7)
    ]
    for i, j in connections:
        x1, y1 = nodes[i][0], nodes[i][1]
        x2, y2 = nodes[j][0], nodes[j][1]
        draw.line([(x1, y1), (x2, y2)], fill=(255, 255, 255, 120), width=int(1.5 * scale))

    # Downsample with Lanczos high quality filter
    final_img = icon.resize((size, size), Image.Resampling.LANCZOS)
    return final_img

# Output directories & sizes
densities = {
    r"D:\AI app\studymate_ai\mobile\android\app\src\main\res\mipmap-mdpi": 48,
    r"D:\AI app\studymate_ai\mobile\android\app\src\main\res\mipmap-hdpi": 72,
    r"D:\AI app\studymate_ai\mobile\android\app\src\main\res\mipmap-xhdpi": 96,
    r"D:\AI app\studymate_ai\mobile\android\app\src\main\res\mipmap-xxhdpi": 144,
    r"D:\AI app\studymate_ai\mobile\android\app\src\main\res\mipmap-xxxhdpi": 192,
}

import os
for path, sz in densities.items():
    os.makedirs(path, exist_ok=True)
    icon = create_studyflow_icon(sz)
    out_file = os.path.join(path, "ic_launcher.png")
    icon.save(out_file, "PNG")
    print(f"Generated {out_file} ({sz}x{sz})")

# In-app branding assets
app_asset_dir = r"D:\AI app\studymate_ai\mobile\assets\icons"
os.makedirs(app_asset_dir, exist_ok=True)
large_icon = create_studyflow_icon(512)
large_icon.save(os.path.join(app_asset_dir, "app_logo.png"), "PNG")
print("Generated 512x512 app_logo.png in assets/icons")

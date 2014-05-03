//-----------------------------------------------------------------------------
// File: texture.cs
//
// Desc: Better than just lights and materials, 3D objects look much more
//       convincing when texture-mapped. Textures can be thought of as a sort
//       of wallpaper, that is shrinkwrapped to fit a texture. Textures are
//       typically loaded from image files, and D3DX provides a utility to
//       function to do this for us. Like a vertex buffer, textures have
//       Lock() and Unlock() functions to access (read or write) the image
//       data. Textures have a width, height, miplevel, and pixel format. The
//       miplevel is for "mipmapped" textures, an advanced performance-
//       enhancing feature which uses lower resolutions of the texture for
//       objects in the distance where detail is less noticeable. The pixel
//       format determines how the colors are stored in a texel. The most
//       common formats are the 16-bit R5G6B5 format (5 bits of red, 6-bits of
//       green and 5 bits of blue) and the 32-bit A8R8G8B8 format (8 bits each
//       of alpha, red, green, and blue).
//
//       Textures are associated with geometry through texture coordinates.
//       Each vertex has one or more sets of texture coordinates, which are
//       named tu and tv and range from 0.0 to 1.0. Texture coordinates can be
//       supplied by the geometry, or can be automatically generated using
//       Direct3D texture coordinate generation (which is an advanced feature).
//
// Copyright (c) Microsoft Corporation. All rights reserved.
//-----------------------------------------------------------------------------
using System;
using System.Drawing;
using System.Windows.Forms;
using Microsoft.DirectX;
using Microsoft.DirectX.Direct3D;
using Direct3D=Microsoft.DirectX.Direct3D;
using Microsoft.Samples.DirectX.UtilityToolkit;
using WiimoteLib;

namespace WiiMultipointGrid
{
    class Point2D
    {
        public float x = 0.0f;
        public float y = 0.0f;
        public void set(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
    }

	public class WiiMultipointGrid : Form
	{        
        // Our global variables for this project
		Device device = null; // Our rendering device

        int numGridlines = 20;
        VertexBuffer gridBuffer = null;
        bool isRendering = false;

        Point2D[] wiimotePointsNormalized = new Point2D[4];
        int[] wiimotePointIDMap = new int[4];

		PresentParameters presentParams = new PresentParameters();
        private Sprite textSprite = null; // Sprite for batching text calls
        private Microsoft.DirectX.Direct3D.Font statsFont = null; // Font for drawing text

        bool isReady = false;
        bool doFullscreen = false;
        int m_dwWidth = 800;
        int m_dwHeight = 600;
        float screenAspect;

        CrosshairCursor mouseCursor;
        int lastFrameTick = 0;
        int frameCount;
        float frameRate = 0;

        Matrix worldTransform = Matrix.Identity;
        Matrix tempWorldTransform = Matrix.Identity;
        
        bool showGrid = true;
        bool showHelp = true;
        bool showMouseCursor = false;

        int lastKey = 0;
        bool mouseDown = false;

        //wiimote stuff
        bool doWiimote = true;
        Wiimote remote;
        CrosshairCursor wiiCursor1;
        CrosshairCursor wiiCursor2;
        CrosshairCursor wiiCursor3;
        CrosshairCursor wiiCursor4;

        bool doWiiCursors = true;
        int leftCursor = 1; //needed for rotation stabilization when two points appear


		public WiiMultipointGrid()
		{
			// Set the initial size of our form
			this.ClientSize = new System.Drawing.Size(800,600);

            screenAspect = m_dwWidth / (float)m_dwHeight;
			this.Text = "Wiimote Multipoint Grid";

            //add event handlers
            this.MouseDown += new System.Windows.Forms.MouseEventHandler(this.OnMouseDown);
            this.MouseUp += new System.Windows.Forms.MouseEventHandler(this.OnMouseUp);
            this.MouseMove += new System.Windows.Forms.MouseEventHandler(this.OnMouseMove);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.OnKeyPress);
            this.FormClosing += new FormClosingEventHandler(this.OnFormClosing);

            for (int i = 0; i < 4; i++)
            {
                wiimotePointsNormalized[i] = new Point2D();
                wiimotePointIDMap[i] = i;
            }
        }

        void OnFormClosing(object sender, FormClosingEventArgs e)
        {
            isReady = false;//set the flag to stop the rendering call driven by incoming wiimote reports
            Cursor.Show();
        }

        private void OnKeyPress(object sender, System.Windows.Forms.KeyEventArgs e)
        {
            lastKey = (int)e.KeyCode;
            if ((int)(byte)e.KeyCode == (int)Keys.Escape)
            {
                isReady = false;
                Cursor.Show();//set the flag to stop the rendering call driven by incoming wiimote reports
                this.Dispose(); // Esc was pressed
                return;
            }
            if ((int)(byte)e.KeyCode == 'G')
                showGrid = !showGrid;
            if ((int)(byte)e.KeyCode == 'H')
                showHelp = !showHelp;
            if ((int)(byte)e.KeyCode == 'M')
                showMouseCursor = !showMouseCursor;
            if ((int)(byte)e.KeyCode == (int)Keys.Up)
            {
            }
            if ((int)(byte)e.KeyCode == (int)Keys.Down)
            {
            }
            if ((int)(byte)e.KeyCode == (int)Keys.Left)
            {
            }
            if ((int)(byte)e.KeyCode == (int)Keys.Right)
            {
            }
        }

        private void OnMouseDown(object sender, System.Windows.Forms.MouseEventArgs e)
        {
            switch (e.Button)
            {
                case MouseButtons.Left:
                    break;
                case MouseButtons.Right:
                    break;
                case MouseButtons.Middle:
                    break;
                default:
                    break;
            }
        }

        private void OnMouseMove(object sender, System.Windows.Forms.MouseEventArgs e)
        {
            mouseCursor.set(screenAspect * (e.X / (float)m_dwWidth - .5f) + .5f, 1.0f - e.Y / (float)m_dwHeight);
            if (mouseDown)//is dragging
            {
            }
        }


        private void OnMouseUp(object sender, System.Windows.Forms.MouseEventArgs e)
        {
            switch (e.Button)
            {
                case MouseButtons.Left:
                    break;
                case MouseButtons.Right:
                    break;
                case MouseButtons.Middle:
                    break;
                default:
                    break;
            }
        }


        private void RenderText()
        {
            TextHelper txtHelper = new TextHelper(statsFont, textSprite, 15);
            txtHelper.Begin();
            txtHelper.SetInsertionPoint(5, 5);

            // Output statistics
            txtHelper.SetForegroundColor(System.Drawing.Color.Yellow);
            txtHelper.DrawTextLine("Stats---------------");
           
            frameCount++;
            if (frameCount == 100)
            {
                frameRate = 100 * 1000.0f / (Environment.TickCount - lastFrameTick);
                lastFrameTick = Environment.TickCount;
                frameCount = 0;
            }

            txtHelper.DrawTextLine("Avg Framerate: " + frameRate);

            txtHelper.DrawTextLine("Wii IR dots:" + remote.WiimoteState.IRState.Found1 + " "
                                                    + remote.WiimoteState.IRState.Found2 + " "
                                                  + remote.WiimoteState.IRState.Found3 + " "
                                                   + remote.WiimoteState.IRState.Found4);
            
            txtHelper.DrawTextLine("Last Key Pressed: " + lastKey);
            txtHelper.DrawTextLine("");

            txtHelper.DrawTextLine("Controls -----");
            txtHelper.DrawTextLine("esc - Quit");
            txtHelper.DrawTextLine("");

            txtHelper.DrawTextLine("Show--------");
            txtHelper.DrawTextLine("M - Mouse Cursor");
            txtHelper.DrawTextLine("G - Grid");
            txtHelper.DrawTextLine("H - Help Text");
            txtHelper.DrawTextLine("");

            txtHelper.End();
        }

		public bool InitializeGraphics()
		{
			try
			{

                AdapterInformation ai = Manager.Adapters.Default;
                Caps caps = Manager.GetDeviceCaps(ai.Adapter, DeviceType.Hardware);
                if (!caps.TextureCaps.SupportsProjected)
                    MessageBox.Show("Erro: Graphics adaptor does not support projected textures!");

                Cursor.Hide();
                presentParams.Windowed=!doFullscreen;
                presentParams.SwapEffect = SwapEffect.Discard; // Discard the frames 
				presentParams.EnableAutoDepthStencil = true; // Turn on a Depth stencil
				presentParams.AutoDepthStencilFormat = DepthFormat.D16; // And the stencil format

                presentParams.BackBufferWidth = m_dwWidth;					//screen width
                presentParams.BackBufferHeight = m_dwHeight;					//screen height
                presentParams.BackBufferFormat = Format.R5G6B5;					//color depth
                presentParams.MultiSample = MultiSampleType.None;				//anti-aliasing
                presentParams.PresentationInterval  = PresentInterval.Immediate; //don't wait... draw right away

                device = new Device(0, DeviceType.Hardware, this, CreateFlags.SoftwareVertexProcessing, presentParams); //Create a device
                device.DeviceReset += new System.EventHandler(this.OnResetDevice);
				this.OnCreateDevice(device, null);
				this.OnResetDevice(device, null);

				return true;
			}
			catch (DirectXException)
			{
				// Catch any errors and return a failure
				return false;
			}

		}

        public void OnCreateDevice(object sender, EventArgs e)
		{
			Device dev = (Device)sender;
            textSprite = new Sprite(dev);
            statsFont = ResourceCache.GetGlobalInstance().CreateFont(dev, 15, 0, FontWeight.Bold, 1, false, CharacterSet.Default,Precision.Default, FontQuality.Default, PitchAndFamily.FamilyDoNotCare | PitchAndFamily.DefaultPitch, "Arial");
             
            //init cursors
            mouseCursor = new CrosshairCursor(dev, 0xffffff, .04f);
            wiiCursor1 = new CrosshairCursor(dev, 0x00ff00, .04f);
            wiiCursor2 = new CrosshairCursor(dev, 0x0000ff, .04f);
            wiiCursor3 = new CrosshairCursor(dev, 0xff0000, .04f);
            wiiCursor4 = new CrosshairCursor(dev, 0xffff00, .04f);

            int step = m_dwWidth / numGridlines;
            gridBuffer = new VertexBuffer(typeof(CustomVertex.PositionColored), 4 * (numGridlines+2), dev, 0, CustomVertex.PositionColored.Format, Pool.Managed);

            CustomVertex.PositionColored[] verts2;
            verts2 = (CustomVertex.PositionColored[])gridBuffer.Lock(0, 0); // Lock the buffer (which will return our structs)
            int vertIndex = 0;
            int gridColor = 0x666666;
            for (int i = 0; i <= numGridlines * 2; i += 2)
            {
                verts2[vertIndex].Position = new Vector3((i*step/2.0f)/m_dwWidth, 0.0f, 0.0f);
                verts2[vertIndex].Color = gridColor;
                vertIndex++;
                verts2[vertIndex].Position = new Vector3((i * step/2.0f)/m_dwWidth, 1.0f, 0.0f);
                verts2[vertIndex].Color = gridColor;
                vertIndex++;
            }
            for (int i = 0; i <= numGridlines * 2; i += 2)
            {
                verts2[vertIndex].Position = new Vector3(0.0f, (i * step / 2.0f)/m_dwWidth, 0.0f);
                verts2[vertIndex].Color = gridColor;
                vertIndex++;
                verts2[vertIndex].Position = new Vector3(1.0f, (i * step / 2.0f)/m_dwWidth, 0.0f);
                verts2[vertIndex].Color = gridColor;
                vertIndex++;
            }
            gridBuffer.Unlock();
            if (doWiimote)
            {
                try
                {
                    remote = new Wiimote();
                    remote.Connect();
                    remote.SetReportType(Wiimote.InputReport.IRAccel, true);
                    remote.GetBatteryLevel();
                    remote.SetLEDs(true, false, false, false);
                    remote.OnWiimoteChanged += new WiimoteChangedEventHandler(wm_OnWiimoteChanged);
                }
                catch (Exception x)
                {
                    MessageBox.Show("Cannot find a wii remote: " + x.Message);
                    doWiimote = false;
                }

            }

		}

        void wm_OnWiimoteChanged(object sender, WiimoteChangedEventArgs args)
        {
            if (isReady)
                Render();
        }

        public void ParseWiimoteData()
        {
            if (remote.WiimoteState == null)
                return;

            if (remote.WiimoteState.IRState.Found1)
            {
                wiimotePointsNormalized[0].x = 1.0f-remote.WiimoteState.IRState.RawX1 / 768.0f;
                wiimotePointsNormalized[0].y = remote.WiimoteState.IRState.RawY1 / 768.0f;
                wiiCursor1.isDown = true;
            }
            else
            {//not visible
                wiiCursor1.isDown = false;
            }
            if (remote.WiimoteState.IRState.Found2)
            {
                wiimotePointsNormalized[1].x = 1.0f - remote.WiimoteState.IRState.RawX2 / 768.0f;
                wiimotePointsNormalized[1].y = remote.WiimoteState.IRState.RawY2 / 768.0f;
                wiiCursor2.isDown = true;
            }
            else
            {//not visible
                wiiCursor2.isDown = false;
            }
            if(remote.WiimoteState.IRState.Found3){
                wiimotePointsNormalized[2].x = 1.0f - remote.WiimoteState.IRState.RawX3 / 768.0f;
                wiimotePointsNormalized[2].y = remote.WiimoteState.IRState.RawY3 / 768.0f;
                wiiCursor3.isDown = true;
            }
            else
            {//not visible
                wiiCursor3.isDown = false;
            }
            if (remote.WiimoteState.IRState.Found4)
            {
                wiimotePointsNormalized[3].x = 1.0f - remote.WiimoteState.IRState.RawX4 / 768.0f;
                wiimotePointsNormalized[3].y = remote.WiimoteState.IRState.RawY4 / 768.0f;
                wiiCursor4.isDown = true;
            }
            else
            {//not visible
                wiiCursor4.isDown = false;
            }


            if (doWiiCursors)
            {
                //wii cursor stuff
                if (wiiCursor1.isDown && !wiiCursor2.isDown) //only point #1
                {
                    if (wiiCursor2.wasDown)//we just lost point #2, so treat as first discovery
                    {
                        wiiCursor1.setDown(wiimotePointsNormalized[0].x, wiimotePointsNormalized[0].y);
                        tempWorldTransform = worldTransform;
                    }

                    if (wiiCursor1.wasDown)
                    {//one point dragging
                        wiiCursor1.set(wiimotePointsNormalized[0].x, wiimotePointsNormalized[0].y);
                        worldTransform = tempWorldTransform * Matrix.Translation(wiiCursor1.X - wiiCursor1.lastX, wiiCursor1.Y - wiiCursor1.lastY, 0.0f);
                    }
                    else
                    {//first discovery
                        wiiCursor1.setDown(wiimotePointsNormalized[0].x, wiimotePointsNormalized[0].y);
                        tempWorldTransform = worldTransform;
                    }
                    wiiCursor1.wasDown = true;
                }

                if (wiiCursor1.isDown && wiiCursor2.isDown)
                {//bimanual control

                    if (!wiiCursor1.wasDown || !wiiCursor2.wasDown)//one of the points was just acquired
                    {
                        wiiCursor1.setDown(wiimotePointsNormalized[0].x, wiimotePointsNormalized[0].y);
                        wiiCursor2.setDown(wiimotePointsNormalized[1].x, wiimotePointsNormalized[1].y);
                        tempWorldTransform = worldTransform;
                        if (wiiCursor1.X < wiiCursor2.X)
                            leftCursor = 1;
                        else
                            leftCursor = 2;
                    }

                    if (wiiCursor2.wasDown)//drag two points
                    {
                        wiiCursor1.set(wiimotePointsNormalized[0].x, wiimotePointsNormalized[0].y);
                        wiiCursor2.set(wiimotePointsNormalized[1].x, wiimotePointsNormalized[1].y);

                        if (leftCursor == 1)
                        {
                            float[,] v = AffineTransformSolver.solve2Dto4x4(
                                                                                    wiiCursor1.lastX,
                                                                                    wiiCursor1.lastY,
                                                                                    wiiCursor2.lastX,
                                                                                    wiiCursor2.lastY,
                                                                                    wiiCursor1.X,
                                                                                    wiiCursor1.Y,
                                                                                    wiiCursor2.X,
                                                                                    wiiCursor2.Y);

                            Matrix t = new Matrix();
                            t.M11 = v[0, 0];
                            t.M21 = v[1, 0];
                            t.M31 = v[2, 0];
                            t.M41 = v[3, 0];

                            t.M12 = v[0, 1];
                            t.M22 = v[1, 1];
                            t.M32 = v[2, 1];
                            t.M42 = v[3, 1];

                            t.M13 = v[0, 2];
                            t.M23 = v[1, 2];
                            t.M33 = v[2, 2];
                            t.M43 = v[3, 2];

                            t.M14 = v[0, 3];
                            t.M24 = v[1, 3];
                            t.M34 = v[2, 3];
                            t.M44 = v[3, 3];

                            worldTransform = tempWorldTransform * t;
                        }
                        else
                        {
                            float[,] v = AffineTransformSolver.solve2Dto4x4(
                                                                                     wiiCursor2.lastX,
                                                                                     wiiCursor2.lastY,
                                                                                     wiiCursor1.lastX,
                                                                                     wiiCursor1.lastY,
                                                                                     wiiCursor2.X,
                                                                                     wiiCursor2.Y,
                                                                                     wiiCursor1.X,
                                                                                     wiiCursor1.Y);

                            Matrix t = new Matrix();
                            t.M11 = v[0, 0];
                            t.M21 = v[1, 0];
                            t.M31 = v[2, 0];
                            t.M41 = v[3, 0];

                            t.M12 = v[0, 1];
                            t.M22 = v[1, 1];
                            t.M32 = v[2, 1];
                            t.M42 = v[3, 1];

                            t.M13 = v[0, 2];
                            t.M23 = v[1, 2];
                            t.M33 = v[2, 2];
                            t.M43 = v[3, 2];

                            t.M14 = v[0, 3];
                            t.M24 = v[1, 3];
                            t.M34 = v[2, 3];
                            t.M44 = v[3, 3];

                            worldTransform = tempWorldTransform * t;
                        }
                    }
                    wiiCursor1.wasDown = true;
                    wiiCursor2.wasDown = true;
                }

                if (wiiCursor2.isDown && !wiiCursor1.isDown)//we only have point #2
                {
                    if (wiiCursor1.wasDown)//we just lost point #1, so treat as first discovery
                    {
                        wiiCursor2.setDown(wiimotePointsNormalized[1].x, wiimotePointsNormalized[1].y);
                        tempWorldTransform = worldTransform;
                    }

                    //do dragging operation (cannot be first discovery if enumerated as point #2)
                    wiiCursor2.set(wiimotePointsNormalized[1].x, wiimotePointsNormalized[1].y);
                    worldTransform = tempWorldTransform * Matrix.Translation(wiiCursor2.X - wiiCursor2.lastX, wiiCursor2.Y - wiiCursor2.lastY, 0.0f);
                }
            }

            //position the graphical cursors at the 3rd and 4th ir points if they exist
            if(wiiCursor3.isDown)
                wiiCursor3.setDown(wiimotePointsNormalized[2].x, wiimotePointsNormalized[2].y);
            if (wiiCursor4.isDown)
                wiiCursor4.setDown(wiimotePointsNormalized[3].x, wiimotePointsNormalized[3].y);


            wiiCursor1.wasDown = wiiCursor1.isDown;
            wiiCursor2.wasDown = wiiCursor2.isDown;
            wiiCursor3.wasDown = wiiCursor3.isDown;
            wiiCursor4.wasDown = wiiCursor4.isDown;
 
        }

		public void OnResetDevice(object sender, EventArgs e)
		{
			Device dev = (Device)sender;
			// Turn off culling, so we see the front and back of the triangle
			dev.RenderState.CullMode = Cull.None;
			// Turn off D3D lighting
			dev.RenderState.Lighting = false;
			// Turn on the ZBuffer
			dev.RenderState.ZBufferEnable = true;
        }
		private void SetupMatrices()
		{

            device.Transform.World = Matrix.Identity;

			// Set up our view matrix. A view matrix can be defined given an eye point,
			// a point to lookat, and a direction for which way is up. Here, we set the
			// eye five units back along the z-axis and up three units, look at the
			// origin, and define "up" to be in the y-direction.
			device.Transform.View = Matrix.LookAtLH( new Vector3( 0.0f, 0.0f,-5.0f ), new Vector3( 0.0f, 0.0f, 0.0f ), new Vector3( 0.0f, 1.0f, 0.0f ) );

			// For the projection matrix, we set up a perspective transform (which
			// transforms geometry from 3D view space to 2D viewport space, with
			// a perspective divide making objects smaller in the distance). To build
			// a perpsective transform, we need the field of view (1/4 pi is common),
			// the aspect ratio, and the near and far clipping planes (which define at
			// what distances geometry should be no longer be rendered).
            device.Transform.Projection = Matrix.OrthoOffCenterLH(.5f - .5f * screenAspect, .5f + .5f * screenAspect, 0.0f, 1.0f, -200.0f, 200.0f);
        }

		private void Render()
		{
            if (isRendering)
                return;
            isRendering = true;

            if (device == null)
                return;
			//Clear the backbuffer to a blue color 
			device.Clear(ClearFlags.Target | ClearFlags.ZBuffer, System.Drawing.Color.Black, 1.0f, 0);
			//Begin the scene
			device.BeginScene();
			// Setup the world, view, and projection matrices
			SetupMatrices();

            if (doWiimote)
            {
                ParseWiimoteData();

                if (doWiiCursors)
                {
                    //draw the cursors
                    if (wiiCursor1.isDown)
                        wiiCursor1.Render(device);
                    if (wiiCursor2.isDown)
                        wiiCursor2.Render(device);
                    if (wiiCursor3.isDown)
                        wiiCursor3.Render(device);
                    if (wiiCursor4.isDown)
                        wiiCursor4.Render(device);
                }
                device.Transform.World = worldTransform;
            }
            if (showGrid)
            {
                device.SetStreamSource(0, gridBuffer, 0);
                device.VertexFormat = CustomVertex.PositionColored.Format;
                device.DrawPrimitives(PrimitiveType.LineList, 0, 2 * (numGridlines + 2));
            }

            if (showMouseCursor)
            {
                device.Transform.World = Matrix.Identity;
                mouseCursor.Render(device);
            }

            if (showHelp)
                RenderText();

            //End the scene
			device.EndScene();

			// Update the screen
			device.Present();
            isRendering = false;
		}

		protected override void OnPaint(System.Windows.Forms.PaintEventArgs e)
		{
            isReady = true;//rendering triggered by wiimote is waiting for this.
		}
        protected override void OnResize(System.EventArgs e)
        {
        }

        /// <summary>
		/// The main entry point for the application.
		/// </summary>
		static void Main() 
		{
            using (WiiMultipointGrid frm = new WiiMultipointGrid())
            {
                if (!frm.InitializeGraphics()) // Initialize Direct3D
                {
                    MessageBox.Show("Could not initialize Direct3D.  This tutorial will exit.");
                    return;
                }
                frm.Show();

                // While the form is still valid, render and process messages
                while(frm.Created)
                {
                   Application.DoEvents();
                }
                Cursor.Show();

            }
		}
	}
}

package com.quanturium.android.library.bottomsheetpicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.aliyun.svideo.recorder.activity.AlivcSvideoRecordActivity;
import com.aliyun.svideo.recorder.bean.AlivcRecordInputParam;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanturium.android.library.multi_select.MultiSelectManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomSheetPickerFragment extends BottomSheetDialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, BottomSheetPickerRecyclerAdapter.OnRecyclerViewEventListener, View.OnClickListener, MultiSelectManager.MultiSelectListener {
	private static final String CAMERA_BUTTON_ENABLED = "camera_button_enabled";
	private static final String FILE_BROWSER_BUTTON_ENABLED = "file_browser_button_enabled";
	private static final String BROWSE_MORE_BUTTON_ENABLED = "browse_more_button_enabled";
	private static final String MULTI_SELECT_ENABLED = "multi_select_enabled";
	private static final String SELECTION_MODE = "selection_mode";
	private static final String MAX_ITEMS = "max_items";
	private static final String CAMERA_ICON_RES_ID = "camera_icon_res_id";

	private static final int DEFAULT_MAX_ITEMS = 25;
	private static final int DEFAULT_SELECTION_MODE = SelectionMode.IMAGES.value;
	private static final int DEFAULT_CAMERA_ICON_RESOURCE = R.drawable.ic_bottomsheetpicker_camera;

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_VIDEO_CAPTURE = 2;
	private static final int REQUEST_FILE_PICKER = 3;

	private static final String SIS_LAYOUT_MANAGER = "savedInstanceStateLayoutManager";
	private static final String SIS_SELECTED_POSITIONS = "savedInstanceStateSelectedPositions";

	private static final String TAG = "BottomSheetPicker";

	private BottomSheetPickerListener listener;
	private boolean isCameraButtonEnabled;
	private boolean isFileBrowserButtonEnabled;
	private boolean isBrowseMoreButtonEnabled;
	private boolean isMultiSelectEnabled;
	private SelectionMode selectionMode;
	private int maxItems;
	private int cameraIconResId;

	private FrameLayout bottomLayout;
	private RecyclerView recyclerView;
	private ImageView cameraButton;
	private Button fileBrowserButton;
	private LinearLayout multiselectLayout;
	private ImageView multiselectCloseImageView;
	private TextView multiselectCountTextView;
	private Button multiselectInsertButton;

	private MultiSelectManager multiSelectManager;
	private BottomSheetPickerRecyclerAdapter cursorAdapter;
	private RecyclerView.LayoutManager layoutManager;

	private Uri cameraUri = null;

	public enum SelectionMode {
		IMAGES(1), VIDEOS(2), IMAGES_AND_VIDEOS(3);

		public final int value;

		SelectionMode(int value) {
			this.value = value;
		}

		private static Map<Integer, SelectionMode> map = new HashMap<>();

		static {
			for (SelectionMode s : SelectionMode.values()) {
				map.put(s.value, s);
			}
		}

		public static SelectionMode valueOf(int selectionMode) {
			return map.get(selectionMode);
		}
	}

	public interface BottomSheetPickerListener {
		void onFileLoad(ImageView imageView, Uri uri);

		void onFilesSelected(List<Uri> uriList);
	}

	public static class Builder {
		private boolean isCameraButtonEnabled = true;
		private boolean isFileBrowserButtonEnabled = true;
		private boolean isBrowseMoreButtonEnabled = true;
		private boolean isMultiSelectEnabled = true;
		private SelectionMode selectionMode;
		private int maxItems = 0;
		private int cameraIconResource = 0;

		public Builder setSelectionMode(SelectionMode selectionMode) {
			this.selectionMode = selectionMode;
			return this;
		}

		public Builder setCameraButtonEnabled(boolean enabled) {
			this.isCameraButtonEnabled = enabled;
			return this;
		}

		public Builder setCameraIcon(@DrawableRes int resId) {
			this.cameraIconResource = resId;
			return this;
		}

		public Builder setFileBrowserButtonEnabled(boolean enabled) {
			this.isFileBrowserButtonEnabled = enabled;
			return this;
		}

		public Builder setMaxItems(int maxItems) {
			this.maxItems = maxItems;
			return this;
		}

		public Builder setBrowseMoreButtonEnabled(boolean enabled) {
			this.isBrowseMoreButtonEnabled = enabled;
			return this;
		}
		public Builder setMultiSelectEnabled(boolean enabled) {
			this.isMultiSelectEnabled = enabled;
			return this;
		}
		public BottomSheetPickerFragment build() {
			BottomSheetPickerFragment fragment = new BottomSheetPickerFragment();
			Bundle args = new Bundle();

			args.putBoolean(CAMERA_BUTTON_ENABLED, isCameraButtonEnabled);
			args.putBoolean(BROWSE_MORE_BUTTON_ENABLED, isBrowseMoreButtonEnabled);
			args.putBoolean(FILE_BROWSER_BUTTON_ENABLED, isFileBrowserButtonEnabled);
			args.putBoolean(MULTI_SELECT_ENABLED, isMultiSelectEnabled);
			if (selectionMode != null)
				args.putInt(SELECTION_MODE, selectionMode.value);
			if (maxItems > 0)
				args.putInt(MAX_ITEMS, maxItems);
			if (cameraIconResource != 0)
				args.putInt(CAMERA_ICON_RES_ID, cameraIconResource);

			fragment.setArguments(args);
			return fragment;
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			isCameraButtonEnabled = args.getBoolean(CAMERA_BUTTON_ENABLED, true);
			isBrowseMoreButtonEnabled = args.getBoolean(BROWSE_MORE_BUTTON_ENABLED, true);
			isFileBrowserButtonEnabled = args.getBoolean(FILE_BROWSER_BUTTON_ENABLED, true);
			isMultiSelectEnabled = args.getBoolean(MULTI_SELECT_ENABLED, true);
			selectionMode = SelectionMode.valueOf(args.getInt(SELECTION_MODE, DEFAULT_SELECTION_MODE));
			maxItems = args.getInt(MAX_ITEMS, DEFAULT_MAX_ITEMS);
			cameraIconResId = args.getInt(CAMERA_ICON_RES_ID, DEFAULT_CAMERA_ICON_RESOURCE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		listener = null;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new BottomSheetDialog(getContext(), getTheme());
	}

	@Override
	@SuppressLint({"InflateParams", "RestrictedApi"})
	public void setupDialog(Dialog dialog, int style) {
		super.setupDialog(dialog, style);
		final View contentView = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheetpicker_base, null);

		recyclerView = (RecyclerView) contentView.findViewById(R.id.bottomsheetpicker_recycler_view);
		fileBrowserButton = (Button) contentView.findViewById(R.id.bottomsheetpicker_file_browser_button);
		cameraButton = (ImageView) contentView.findViewById(R.id.bottomsheetpicker_camera_button);
		bottomLayout = (FrameLayout) contentView.findViewById(R.id.bottomsheetpicker_bottom_layout);
		multiselectLayout = (LinearLayout) contentView.findViewById(R.id.bottomsheetpicker_bottom_multiselect_layout);
		multiselectCloseImageView = (ImageView) contentView.findViewById(R.id.bottomsheetpicker_bottom_multiselect_close_image_view);
		multiselectCountTextView = (TextView) contentView.findViewById(R.id.bottomsheetpicker_bottom_multiselect_count_text_view);
		multiselectInsertButton = (Button) contentView.findViewById(R.id.bottomsheetpicker_bottom_multiselect_insert_button);

		if (isFileBrowserButtonEnabled) {
			fileBrowserButton.setVisibility(View.VISIBLE);
			fileBrowserButton.setOnClickListener(this);
		}

		if (isCameraButtonEnabled) {
			cameraButton.setVisibility(View.VISIBLE);
			cameraButton.setOnClickListener(this);
			cameraButton.setImageResource(cameraIconResId);
		}

		multiselectCloseImageView.setOnClickListener(this);
		multiselectInsertButton.setOnClickListener(this);

		dialog.setContentView(contentView);

		contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {

				contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(((View) contentView.getParent()));
				bottomSheetBehavior.setPeekHeight(contentView.getHeight());
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG,"onActivityCreated");
		cursorAdapter = new BottomSheetPickerRecyclerAdapter(recyclerView.getContext(), this, isBrowseMoreButtonEnabled, maxItems);
		multiSelectManager = new MultiSelectManager();
		multiSelectManager.setMultiSelectComponent(cursorAdapter);
		multiSelectManager.setSelectable(isMultiSelectEnabled);
		multiSelectManager.setListener(this);
		layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		this.recyclerView.setLayoutManager(layoutManager);
		this.recyclerView.setAdapter(cursorAdapter);
		this.recyclerView.setHasFixedSize(true);
		this.recyclerView.setItemAnimator(null);

		if (savedInstanceState != null) {

			// Restore the scrolling of the RecyclerView
			Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(SIS_LAYOUT_MANAGER);
			recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

			// Restore the selected items
			Bundle savedSelectedIdsBundle = savedInstanceState.getBundle(SIS_SELECTED_POSITIONS);
			multiSelectManager.restoreMultiSelectStates(savedSelectedIdsBundle);
			onMultiSelectSelectionChanged(false, multiSelectManager.isSelectable(), multiSelectManager.getSelectedCount());
		}

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(SIS_LAYOUT_MANAGER, recyclerView.getLayoutManager().onSaveInstanceState());
		outState.putBundle(SIS_SELECTED_POSITIONS, multiSelectManager.saveMultiSelectStates());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG,"onActivityResult");
		if (resultCode == Activity.RESULT_OK) {
			dismiss();
			List<Uri> selectedFiles = new ArrayList<>();
			if (requestCode == REQUEST_FILE_PICKER && data != null) {
				ClipData clipData;

				clipData = data.getClipData();

				if (clipData != null) { // We are dealing with multiple Uris
					for (int i = 0; i < clipData.getItemCount(); i++)
						selectedFiles.add(clipData.getItemAt(i).getUri());
				} else { // Not clipData. Either the app did not send back any data or it is only one file that can be accessed through getData()
					Uri selectedFile = data.getData();
					if (selectedFile != null)
						Log.d("收到了地址信息",selectedFile.toString());
						selectedFiles.add(selectedFile);
				}
			} else if (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_VIDEO_CAPTURE) {
				if (cameraUri != null) {
					Log.d("收到了地址信息",cameraUri.toString());
					selectedFiles.add(cameraUri);
				}
			}

			if (selectedFiles.size() > 0 && listener != null)
				listener.onFilesSelected(selectedFiles);
		}
	}

	public void setListener(BottomSheetPickerListener listener) {
		this.listener = listener;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Add local images, in descending order of date taken
		String[] projection = new String[]{
				MediaStore.Files.FileColumns._ID,
				MediaStore.Files.FileColumns.DATA,
				MediaStore.Files.FileColumns.MEDIA_TYPE
		};

		String selection = null;

		switch (selectionMode) {
			case IMAGES:
				selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
						+ MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
				break;

			case VIDEOS:
				selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
						+ MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
				break;

			case IMAGES_AND_VIDEOS:
				selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
						+ MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
						+ " OR "
						+ MediaStore.Files.FileColumns.MEDIA_TYPE + "="
						+ MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
				break;
		}

		return new CursorLoader(this.getActivity(), MediaStore.Files.getContentUri("external"), projection, selection,
				null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			if (cursorAdapter != null)
				cursorAdapter.swapCursor(cursor);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (cursorAdapter != null)
			cursorAdapter.swapCursor(null);
	}

	@Override
	public void onClick(View v) {
		Log.d("收到了地址信息","onClick");
		int i = v.getId();
		if (i == R.id.bottomsheetpicker_camera_button) {
			MyFileVariable.getInstance().setTakeFrom(MyFileVariable.TAKE_FROM_CAMERA);
			Log.d("收到了地址信息","来自相机");
			switch (selectionMode) {
				case IMAGES:

					cameraUri = IntentUtils.sendCameraImageIntent(BottomSheetPickerFragment.this, REQUEST_IMAGE_CAPTURE);

					break;

				case VIDEOS:

					//cameraUri = IntentUtils.sendCameraVideoIntent(BottomSheetPickerFragment.this, REQUEST_VIDEO_CAPTURE);
					//改为阿里云录制
					AlivcRecordInputParam recordParam1 = new AlivcRecordInputParam.Builder().build();
					AlivcSvideoRecordActivity.startRecord(getActivity(), recordParam1);

					break;

				case IMAGES_AND_VIDEOS:

					PopupMenu popupMenu = new PopupMenu(getActivity(), cameraButton);
					popupMenu.inflate(R.menu.bottomsheetpicker_camera_menu);
					popupMenu.setOnMenuItemClickListener(item -> {
						int id = item.getItemId();
						if (id == R.id.menu_package_compose_tile_camera_photo) {
							cameraUri = IntentUtils.sendCameraImageIntent(BottomSheetPickerFragment.this, REQUEST_IMAGE_CAPTURE);
						} else if (id == R.id.menu_package_compose_tile_camera_video) {
							//cameraUri = IntentUtils.sendCameraVideoIntent(BottomSheetPickerFragment.this, REQUEST_VIDEO_CAPTURE);
							//改为阿里云录制
							AlivcRecordInputParam recordParam2 = new AlivcRecordInputParam.Builder().build();
							AlivcSvideoRecordActivity.startRecord(getActivity(), recordParam2);
						}

						return true;
					});
					popupMenu.show();

					break;
			}
		} else if (i == R.id.bottomsheetpicker_file_browser_button) {
			MyFileVariable.getInstance().setTakeFrom(MyFileVariable.TAKE_FROM_FOLDER);
			Log.d("收到了地址信息","来自文件夹");
			IntentUtils.sendFileBrowserIntent(BottomSheetPickerFragment.this, REQUEST_FILE_PICKER);

		} else if (i == R.id.bottomsheetpicker_bottom_multiselect_close_image_view) {
			multiSelectManager.clearSelectedPositions();
		} else if (i == R.id.bottomsheetpicker_bottom_multiselect_insert_button) {
			MyFileVariable.getInstance().setTakeFrom(MyFileVariable.TAKE_FROM_SHEET);
			Log.d("收到了地址信息","来自选单");
			if (listener != null) {
				listener.onFilesSelected(cursorAdapter.getItems(multiSelectManager.getSelectedPositions()));
			}
		}
	}

	@Override
	public void onTileLoad(ImageView imageView, Uri uri) {
		if (listener != null)
			listener.onFileLoad(imageView, uri);
	}

	@Override
	public void onTileSelected(Uri uri) {
		List<Uri> selectedFiles = new ArrayList<>();
		selectedFiles.add(uri);
		if (listener != null)
			listener.onFilesSelected(selectedFiles);
	}

	@Override
	public void onMultiSelectSelectionChanged(boolean stateChanged, boolean isSelectable, int selectedCount) {
		if (stateChanged) {
			if (isSelectable) {
				multiselectLayout.setVisibility(View.VISIBLE);
				multiselectLayout.setAlpha(0);
				multiselectLayout.animate()
						.alpha(1)
						.setInterpolator(new FastOutSlowInInterpolator())
						.setDuration(200)
						.setListener(null)
						.start();
			} else {
				multiselectLayout.animate()
						.alpha(0)
						.setInterpolator(new FastOutSlowInInterpolator())
						.setDuration(200)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								multiselectLayout.setVisibility(View.GONE);
							}
						})
						.start();
			}

		} else {
			if (isSelectable) {
				multiselectLayout.setVisibility(View.VISIBLE);
			} else {
				multiselectLayout.setVisibility(View.GONE);
			}
		}

		multiselectCountTextView.setText(getString(R.string.bottomsheetpicker_selected_count, selectedCount));
	}
}

package com.podevs.android.pokemononline.teambuilder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.podevs.android.pokemononline.R;
import com.podevs.android.pokemononline.player.PlayerProfile;
import com.podevs.android.pokemononline.poke.Team;
import com.podevs.android.pokemononline.pokeinfo.InfoConfig;
import com.podevs.android.utilities.QColor;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TrainerFragment extends Fragment {
	protected static final String TAG = "Trainer menu";
	PlayerProfile p = null;
	boolean profileChanged = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		p = new PlayerProfile(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.teambuilder_root, container, false);

		Button importbutton = (Button)v.findViewById(R.id.importteambutton);
		//Register onClick listener
		importbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((TeambuilderActivity)getActivity()).onImportClicked();
			}
		});

		Button editTeamButton = (Button)v.findViewById(R.id.editteam);
		editTeamButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				((TeambuilderActivity)getActivity()).viewPager.setCurrentItem(1, true);	
			}
		});

		((EditText)v.findViewById(R.id.name)).append(p.nick);
		((EditText)v.findViewById(R.id.trainerInfo)).setText(p.trainerInfo.info);
		((EditText)v.findViewById(R.id.winning_message)).setText(p.trainerInfo.winMsg);
		((EditText)v.findViewById(R.id.losing_message)).setText(p.trainerInfo.loseMsg);
		((EditText)v.findViewById(R.id.teamTier)).setText(((TeambuilderActivity)getActivity()).team.defaultTier);

		Button colorButton = (Button)v.findViewById(R.id.color);
		colorButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("org.openintents.action.PICK_COLOR");
				intent.putExtra("org.openintents.extra.COLOR", p.color.colorInt);

				try {
					startActivityForResult(intent, TeambuilderActivity.PICKCOLOR_RESULT_CODE);
				} catch (ActivityNotFoundException e) {
					showDownloadDialog();
				}
			}
		});
		
		colorButton.setTag(R.id.color, p.color.colorInt);
		
		final ViewPager avatars = (ViewPager)v.findViewById(R.id.avatarChooser);
		avatars.setCurrentItem(p.trainerInfo.avatar);
		avatars.setAdapter(new AvatarAdapter());

		avatars.setOnPageChangeListener(new OnPageChangeListener() {
			private View selected = null;
			
			public void onPageSelected(int pos) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					View current = avatars.getChildAt(avatars.getCurrentItem());
					
					if (current == selected) {
						return;
					}
					
					if (selected != null) {
						selected.setAlpha(0.4f);
					}
					selected = avatars.getChildAt(avatars.getCurrentItem());
					selected.setAlpha(1.0f);
				}
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			public void onPageScrollStateChanged(int pos) {
				//this.onPageSelected(pos);
			}
		});
		
		return v;
	}
	
	private class AvatarAdapter extends PagerAdapter {
		SparseArray<View> items = new SparseArray<View>();
		
		@Override
		public int getCount() {
			return 301;
		}

		@Override
		public float getPageWidth(int position) {
			return 0.25f;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Resources resources = InfoConfig.resources;
			int id =  resources.getIdentifier("t" + position, "drawable", InfoConfig.pkgName);
			ImageView v = new ImageView(getActivity());
			v.setImageResource(id);

			container.addView(v);
			items.put(position, v);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && items.size() > 1) {
				v.setAlpha(0.4f);
			}
			
			return v;
			
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (Object)arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
			items.remove(position);
		}
	}

	private AlertDialog showDownloadDialog() {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(getActivity());
		downloadDialog.setTitle("Download color picker");
		downloadDialog.setMessage("The color picker app doesn't seem to be installed!");
		downloadDialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialogInterface, int i) {
				String packageName = "org.openintents.colorpicker";
				Uri uri = Uri.parse("market://details?id=" + packageName);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException anfe) {
					// Hmm, market is not installed
					Log.w(TAG, "Google Play is not installed; cannot install " + packageName);
				}
			}
		});
		downloadDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {}
		});
		return downloadDialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TeambuilderActivity.PICKCOLOR_RESULT_CODE && resultCode == Activity.RESULT_OK) {
			int colorInt = data.getIntExtra("org.openintents.extra.COLOR", p.color.colorInt);
			p.color = new QColor(colorInt);
			profileChanged = true;
		}
	}

	@Override
	public void onPause() {
		/* Implement user changes */
		PlayerProfile p2 = new PlayerProfile();
		View v = getView();
		p2.nick = ((EditText)v.findViewById(R.id.name)).getText().toString();
		p2.trainerInfo.info = ((EditText)v.findViewById(R.id.trainerInfo)).getText().toString();
		p2.trainerInfo.winMsg = ((EditText)v.findViewById(R.id.winning_message)).getText().toString();
		p2.trainerInfo.loseMsg = ((EditText)v.findViewById(R.id.losing_message)).getText().toString();
		p2.color = p.color;
		p2.trainerInfo.avatar = (short) ((ViewPager)v.findViewById(R.id.avatarChooser)).getCurrentItem();

		if (profileChanged || !p2.equals(p)) {
			getActivity().setResult(Activity.RESULT_OK);
			p2.save(getActivity());
			p = p2;
			profileChanged = false;
		}

		if (!((EditText)v.findViewById(R.id.teamTier)).getText().toString()
				.equals(getTeam().defaultTier)) {
			getTeam().defaultTier = ((EditText)v.findViewById(R.id.teamTier)).getText().toString();
			getTeam().save(getActivity());
		}

		super.onPause();
	}

	private Team getTeam() {
		return ((TeambuilderActivity)getActivity()).team;
	}

	public void updateTeam() {
		((EditText)getView().findViewById(R.id.teamTier)).setText(getTeam().defaultTier);
	}
}

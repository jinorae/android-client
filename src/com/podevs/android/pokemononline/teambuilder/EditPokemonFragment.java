package com.podevs.android.pokemononline.teambuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.podevs.android.pokemononline.R;
import com.podevs.android.pokemononline.battle.ListedPokemon;
import com.podevs.android.pokemononline.poke.TeamPoke;
import com.podevs.android.pokemononline.poke.UniqueID;
import com.podevs.android.pokemononline.teambuilder.PokemonChooserFragment.PokemonChooserListener;

public class EditPokemonFragment extends Fragment implements PokemonChooserListener {
	private ListedPokemon pokeList;
	private TeamPoke poke = null;
	private ViewPager pager = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.edit_pokemon, container, false);
		
		pokeList = new ListedPokemon((RelativeLayout)v.findViewById(R.id.pokeViewLayout));
		
		pager = (ViewPager)v.findViewById(R.id.editpokeviewpager);
		pager.setAdapter(new EditPokeAdapter(getFragmentManager()));
		
		setPoke(activity().team.poke(activity().currentPoke));
		
		pokeList.setOnImageClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				pager.setCurrentItem(0);
			}
		});
		
		return v;
	}
	
	public void setPoke(TeamPoke poke) {
		this.poke = poke;
		updatePoke();
	}
	
	public void updatePoke() {
		pokeList.update(poke);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private TeambuilderActivity activity() {
		return (TeambuilderActivity) getActivity();
	}
	
	class EditPokeAdapter extends FragmentPagerAdapter {

		public EditPokeAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			if (arg0 == 0) {
				PokemonChooserFragment ret = new PokemonChooserFragment();
				ret.setOnPokemonChosenListener(EditPokemonFragment.this);
				return ret;
			} else {
				return new MoveChooserFragment();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
		
	}

	public void onPokemonChosen(UniqueID id, String nickname) {
		poke.setNum(id);
		if (nickname.length() > 0) {
			poke.nick = nickname;
		}
		
		updatePoke();
		
		pager.setCurrentItem(1);
	}
}

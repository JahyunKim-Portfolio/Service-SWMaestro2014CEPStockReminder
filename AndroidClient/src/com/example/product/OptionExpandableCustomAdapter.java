package com.example.product;

import java.util.ArrayList;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.product.R;

public class OptionExpandableCustomAdapter extends BaseExpandableListAdapter {
	Context context;
	ArrayList<Option> option;
	ArrayList<OptionData> data;

	public OptionExpandableCustomAdapter(Context contexts,
			ArrayList<Option> options, ArrayList<OptionData> datas) {
		context = contexts;
		option = options;
		data = datas;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return option.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (option.get(groupPosition).getType() >= 2)
			return 0;
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return option.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return option.get(groupPosition).introduct;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return super.areAllItemsEnabled();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, final boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.expandablelistview_costum_option_group, parent,
					false);
		}
		ImageView indicator = (ImageView) convertView
				.findViewById(R.id.imgIndicator);
		if (isExpanded)
			indicator.setImageResource(R.drawable.ic_action_collapse_light);
		else
			indicator.setImageResource(R.drawable.ic_action_expand_light);
		//indicator.setImageResource(drawable.ic_menu_more);
		Button btnStatus = (Button) convertView.findViewById(R.id.btnDelete);
		TextView txtOptionName = (TextView) convertView
				.findViewById(R.id.txtOptionName);
		TextView txtOptionIntroduct = (TextView) convertView
				.findViewById(R.id.txtOptionIntroduct);
		btnStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int now = (Integer) v.getTag();
				option.remove(now);
				data.remove(now);
				OptionExpandableCustomAdapter.this.notifyDataSetChanged();
				// TODO Auto-generated method stub
			}
		});

		// btnStatus.setBackgroundColor(Color.WHITE);
		btnStatus.setBackgroundColor(Color.TRANSPARENT);
		btnStatus.setBackgroundResource(R.drawable.ic_action_remove_light);
		btnStatus.setTag(groupPosition);
		LinearLayout ll = (LinearLayout) convertView
				.findViewById(R.id.llOption);
		if (option.get(groupPosition).getType() == 0) {
			//txtOptionIntroduct.setBackgroundColor(Color.parseColor("#F0FFF0"));
			txtOptionIntroduct.setBackgroundResource(R.color.condition_type_easy_light);
			//ll.setBackgroundColor(Color.parseColor("#008000"));
			ll.setBackgroundResource(R.color.condition_type_easy);
		} else {
			//txtOptionIntroduct.setBackgroundColor(Color.parseColor("#FFDDFF"));
			txtOptionIntroduct.setBackgroundResource(R.color.condition_type_hard_light);
			//ll.setBackgroundColor(Color.parseColor("#660000"));
			ll.setBackgroundResource(R.color.condition_type_hard);
		}
		txtOptionName.setText(option.get(groupPosition).getName());
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		LayoutInflater infalInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (option.get(groupPosition).getType() == 0) {
			convertView = infalInflater.inflate(
					R.layout.expandablelistview_costum_option_child, parent,
					false);
			//convertView.setBackgroundColor(Color.parseColor("#F0FFF0"));
			convertView.setBackgroundResource(R.color.condition_type_easy_light);
			TextView txtOptionDetail = (TextView) convertView
					.findViewById(R.id.txtOptionDetail);
			txtOptionDetail.setText(option.get(groupPosition).getDetail());
		} else {
			convertView = infalInflater.inflate(
					R.layout.expandablelistview_costum_option_child2, parent,
					false);
			//convertView.setBackgroundColor(Color.parseColor("#FFDDFF"));
			convertView.setBackgroundResource(R.color.condition_type_hard_light);
			LinearLayout inLayout = (LinearLayout) convertView
					.findViewById(R.id.llChild);
			for (int i = 0; i < 2; i++) {
				final int x = i;
				LinearLayout route = (LinearLayout) infalInflater.inflate(
						R.layout.expandablelistview_child2_child, null);
				final EditText txtParamName1 = (EditText) route
						.findViewById(R.id.txtParamName1);
				final EditText txtParamName2 = (EditText) route
						.findViewById(R.id.txtParamName2);
				TextView txtParam1 = (TextView) route
						.findViewById(R.id.txtParam1);
				TextView txtParam2 = (TextView) route
						.findViewById(R.id.txtParam2);
				txtParam1.setText((char) ('A' + 2 * i) + "");
				txtParam2.setText((char) ('A' + 2 * i + 1) + "");
				txtParamName1.setText(data.get(groupPosition).getParam(2 * i));
				txtParamName2.setText(data.get(groupPosition).getParam(
						2 * i + 1));
				txtParamName1.setHint("인자" + (char) ('A' + 2 * i));
				txtParamName2.setHint("인자" + (char) ('A' + 2 * i + 1));
				txtParamName1.setEnabled(true);
				txtParamName2.setEnabled(true);

				txtParamName1.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						data.get(groupPosition).setParam(x * 2, txtParamName1.getText().toString());
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
					}
				});
				txtParamName2.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						data.get(groupPosition).setParam(x * 2 + 1, txtParamName2.getText().toString());
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
					}
				});
				inLayout.addView(route);
			}
		}
		// TODO Auto-generated method stub
		return convertView;
	}

}

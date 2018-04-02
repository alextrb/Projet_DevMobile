package app.univers7.ultra_instinct;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class EditMatchScoreFragment extends Fragment {

    private TextView tv_score_p1, tv_score_p2;
    private TextView tv_r1_p1, tv_r2_p1, tv_r3_p1, tv_r4_p1, tv_r5_p1, tv_r1_p2, tv_r2_p2, tv_r3_p2, tv_r4_p2, tv_r5_p2;

    private OnFragmentInteractionListener mListener;

    public EditMatchScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_match_score, container, false);
        tv_score_p1 = (TextView) view.findViewById(R.id.tv_score_p1);
        tv_score_p2 = (TextView) view.findViewById(R.id.tv_score_p2);
        tv_r1_p1 = (TextView) view.findViewById(R.id.tv_r1_p1);
        tv_r2_p1 = (TextView) view.findViewById(R.id.tv_r2_p1);
        tv_r3_p1 = (TextView) view.findViewById(R.id.tv_r3_p1);
        tv_r4_p1 = (TextView) view.findViewById(R.id.tv_r4_p1);
        tv_r5_p1 = (TextView) view.findViewById(R.id.tv_r5_p1);
        tv_r1_p2 = (TextView) view.findViewById(R.id.tv_r1_p2);
        tv_r2_p2 = (TextView) view.findViewById(R.id.tv_r2_p2);
        tv_r3_p2 = (TextView) view.findViewById(R.id.tv_r3_p2);
        tv_r4_p2 = (TextView) view.findViewById(R.id.tv_r4_p2);
        tv_r5_p2 = (TextView) view.findViewById(R.id.tv_r5_p2);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void initializeScore(String p1, String p2,
                                int p1r1, int p1r2, int p1r3, int p1r4, int p1r5,
                                int p2r1, int p2r2, int p2r3, int p2r4, int p2r5)
    {
        tv_score_p1.setText(p1);
        tv_score_p2.setText(p2);
        tv_r1_p1.setText(String.valueOf(p1r1));
        tv_r2_p1.setText(String.valueOf(p1r1 + p1r2));
        tv_r3_p1.setText(String.valueOf(p1r1 + p1r2 + p1r3));
        tv_r4_p1.setText(String.valueOf(p1r1 + p1r2 + p1r3 + p1r4));
        tv_r5_p1.setText(String.valueOf(p1r1 + p1r2 + p1r3 + p1r4 + p1r5));
        tv_r1_p2.setText(String.valueOf(p2r1));
        tv_r2_p2.setText(String.valueOf(p2r1 + p2r2));
        tv_r3_p2.setText(String.valueOf(p2r1 + p2r2 + p2r3));
        tv_r4_p2.setText(String.valueOf(p2r1 + p2r2 + p2r3 + p2r4));
        tv_r5_p2.setText(String.valueOf(p2r1 + p2r2 + p2r3 + p2r4 + p2r5));
    }

    public void updateScore(int round_number, String player, int value)
    {
        switch(round_number)
        {
            case 1:
            {
                if(player == "p1")
                {
                    tv_r1_p1.setText(String.valueOf(value));
                }
                else if(player == "p2")
                {
                    tv_r1_p2.setText(String.valueOf(value));
                }
                break;
            }

            case 2:
            {
                if(player == "p1")
                {
                    tv_r2_p1.setText(String.valueOf(value));
                }
                else if(player == "p2")
                {
                    tv_r2_p2.setText(String.valueOf(value));
                }
                break;
            }

            case 3:
            {
                if(player == "p1")
                {
                    tv_r3_p1.setText(String.valueOf(value));
                }
                else if(player == "p2")
                {
                    tv_r3_p2.setText(String.valueOf(value));
                }
                break;
            }

            case 4:
            {
                if(player == "p1")
                {
                    tv_r4_p1.setText(String.valueOf(value));
                }
                else if(player == "p2")
                {
                    tv_r4_p2.setText(String.valueOf(value));
                }
                break;
            }

            case 5:
            {
                if(player == "p1")
                {
                    tv_r5_p1.setText(String.valueOf(value));
                }
                else if(player == "p2")
                {
                    tv_r5_p2.setText(String.valueOf(value));
                }
                break;
            }
        }
    }

    public void setWinnerRowColor(String winner)
    {
        if(winner == "p1")
        {
            tv_score_p1.setBackgroundColor(Color.parseColor("#437932"));
            tv_score_p2.setBackgroundColor(Color.parseColor("#a85a5a"));
        }
        else if(winner == "p2")
        {
            tv_score_p2.setBackgroundColor(Color.parseColor("#437932"));
            tv_score_p1.setBackgroundColor(Color.parseColor("#a85a5a"));
        }
    }
}

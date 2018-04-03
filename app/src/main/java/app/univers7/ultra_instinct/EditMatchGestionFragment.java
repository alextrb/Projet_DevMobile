package app.univers7.ultra_instinct;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class EditMatchGestionFragment extends Fragment {

    private TextView tv_gestion_p1, tv_gestion_p2;

    private Button edit_j1_bk, edit_j1_sbk, edit_j1_hk, edit_j1_shk, edit_j1_punch, edit_j1_KO, edit_j1_faute,
            edit_j2_bk, edit_j2_sbk, edit_j2_hk, edit_j2_shk, edit_j2_punch, edit_j2_KO, edit_j2_faute,
            btn_gestion_nextround;

    private OnFragmentInteractionListener mListener;

    public EditMatchGestionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_match_gestion, container, false);

        tv_gestion_p1 = (TextView) view.findViewById(R.id.tv_gestion_p1);
        tv_gestion_p2 = (TextView) view.findViewById(R.id.tv_gestion_p2);

        edit_j1_bk = (Button)view.findViewById(R.id.edit_j1_bk);
        edit_j1_bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j1_bk");
                }
            }
        });
        edit_j1_sbk = (Button)view.findViewById(R.id.edit_j1_sbk);
        edit_j1_sbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j1_sbk");
                }
            }
        });
        edit_j1_hk = (Button)view.findViewById(R.id.edit_j1_hk);
        edit_j1_hk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j1_hk");
                }
            }
        });
        edit_j1_shk = (Button)view.findViewById(R.id.edit_j1_shk);
        edit_j1_shk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j1_shk");
                }
            }
        });
        edit_j1_punch = (Button)view.findViewById(R.id.edit_j1_punch);
        edit_j1_punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j1_punch");
                }
            }
        });
        edit_j1_KO = (Button)view.findViewById(R.id.edit_j1_KO);
        edit_j1_KO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j1_KO");
                }
            }
        });
        edit_j1_faute = (Button)view.findViewById(R.id.edit_j1_faute);
        edit_j1_faute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j1_faute");
                }
            }
        });
        edit_j2_bk = (Button)view.findViewById(R.id.edit_j2_bk);
        edit_j2_bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j2_bk");
                }
            }
        });
        edit_j2_sbk = (Button)view.findViewById(R.id.edit_j2_sbk);
        edit_j2_sbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j2_sbk");
                }
            }
        });
        edit_j2_hk = (Button)view.findViewById(R.id.edit_j2_hk);
        edit_j2_hk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j2_hk");
                }
            }
        });
        edit_j2_shk = (Button)view.findViewById(R.id.edit_j2_shk);
        edit_j2_shk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j2_shk");
                }
            }
        });
        edit_j2_punch = (Button)view.findViewById(R.id.edit_j2_punch);
        edit_j2_punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j2_punch");
                }
            }
        });
        edit_j2_KO = (Button)view.findViewById(R.id.edit_j2_KO);
        edit_j2_KO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j2_KO");
                }
            }
        });
        edit_j2_faute = (Button)view.findViewById(R.id.edit_j2_faute);
        edit_j2_faute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.gestionButtonClicked("edit_j2_faute");
                }
            }
        });
        btn_gestion_nextround = (Button)view.findViewById(R.id.btn_gestion_nextround);
        btn_gestion_nextround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.gestionButtonClicked("btn_gestion_nextround");
                }


            }
        });

        return view;
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
        void gestionButtonClicked(String button_name);
    }

    public void setNextRound(int roundNumber)
    {
        if(roundNumber<6)
        {
            btn_gestion_nextround.setText("FIN MANCHE " + String.valueOf(roundNumber));
        }
        else if(roundNumber == 6)
        {
            btn_gestion_nextround.setText("MATCH TERMINÉ");
            edit_j1_bk.setEnabled(false);
            edit_j1_sbk.setEnabled(false);
            edit_j1_hk.setEnabled(false);
            edit_j1_shk.setEnabled(false);
            edit_j1_punch.setEnabled(false);
            edit_j1_KO.setEnabled(false);
            edit_j1_faute.setEnabled(false);
            edit_j2_bk.setEnabled(false);
            edit_j2_sbk.setEnabled(false);
            edit_j2_hk.setEnabled(false);
            edit_j2_shk.setEnabled(false);
            edit_j2_punch.setEnabled(false);
            edit_j2_KO.setEnabled(false);
            edit_j2_faute.setEnabled(false);
            btn_gestion_nextround.setEnabled(false);
        }
    }

    public void setPlayersName(String player1, String player2)
    {
        tv_gestion_p1.setText(player1);
        tv_gestion_p2.setText(player2);
    }

    public void setStatus(int match_status)
    {
        if(match_status == 1)
        {
            btn_gestion_nextround.setText("MATCH TERMINÉ");
            edit_j1_bk.setEnabled(false);
            edit_j1_sbk.setEnabled(false);
            edit_j1_hk.setEnabled(false);
            edit_j1_shk.setEnabled(false);
            edit_j1_punch.setEnabled(false);
            edit_j1_KO.setEnabled(false);
            edit_j1_faute.setEnabled(false);
            edit_j2_bk.setEnabled(false);
            edit_j2_sbk.setEnabled(false);
            edit_j2_hk.setEnabled(false);
            edit_j2_shk.setEnabled(false);
            edit_j2_punch.setEnabled(false);
            edit_j2_KO.setEnabled(false);
            edit_j2_faute.setEnabled(false);
            btn_gestion_nextround.setEnabled(false);
        }
    }
}

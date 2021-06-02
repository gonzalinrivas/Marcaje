package com.example.listadofichajes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.listadofichajes.Marcaje.*
import com.example.listadofichajes.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: MarcajesAdapter
    private lateinit var mGridLayout: GridLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        mBinding.fab.setOnClickListener { launchEditFragment() }

        setRecyclerView()

    }
//LANZAR FRAGMENTO EDIT_MARCAJE_FRAGMENT------------------------------------------------------------


    private fun launchEditFragment(args: Bundle? = null) {
        val fragment = EditMarcajeFragment()
        if (args != null) fragment.arguments = args


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


        hideFab()

    }

    private fun setRecyclerView() {
        mAdapter = MarcajesAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 1)
        getMarcajes()

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getMarcajes() {
        doAsync {
            val darMarcajes = MarcajeApplication.database.marcajeDao().getAllMarcajes()
            uiThread {
                mAdapter.setMarcajes(darMarcajes)
            }
        }

    }


    /*
    OnClickListener
     */
    override fun onClick(marcajesId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.arg_id), marcajesId)


        launchEditFragment(args)

    }

    //MARCAJE COMO FAVORITO-----------------------------------------------------------------------------
    override fun onFavoriteMarcaje(marcajesEntity: MarcajesEntity) {
        marcajesEntity.isFavorite = !marcajesEntity.isFavorite
        doAsync {
            MarcajeApplication.database.marcajeDao().updateMarcaje(marcajesEntity)
            uiThread {
                updateMarcaje(marcajesEntity)
            }
        }


    }

    //BORRAR MARCAJE--------------------------------------------------------------------------------

    override fun onDeleteMarcaje(marcajesEntity: MarcajesEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(R.string.dialog_delete_confirm, { dialogInterface, i ->
                doAsync {
                    MarcajeApplication.database.marcajeDao().deleteMarcaje(marcajesEntity)
                    uiThread {
                        mAdapter.delete(marcajesEntity)
                    }
                }
            })
            .setNegativeButton(R.string.dialog_delete_cancel, null)
            .show()


    }


    //Ocultar FloatingActionButton
    override fun hideFab(isVisible: Boolean) {
        if (isVisible) mBinding.fab.show() else mBinding.fab.hide()
    }

    //AÑADIR MARCAJE--------------------------------------------------------------------------------
    override fun addMarcaje(marcajesEntity: MarcajesEntity) {
        mAdapter.add(marcajesEntity)
    }

    //ACTUALIZAR MARCAJE--------------------------------------------------------------------------------
    override fun updateMarcaje(marcajesEntity: MarcajesEntity) {
        mAdapter.update(marcajesEntity)
    }
}
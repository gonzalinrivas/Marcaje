package com.example.listadofichajes.Marcaje

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.listadofichajes.MainActivity
import com.example.listadofichajes.R
import com.example.listadofichajes.TimePickerFragment
import com.example.listadofichajes.databinding.FragmentEditMarcajeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class EditMarcajeFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private lateinit var mBinding: FragmentEditMarcajeBinding
    private var mActivity: MainActivity? = null

    private var mIsEditMode: Boolean = false
    private var mMarcajesEntity: MarcajesEntity? = null

    private lateinit var spinnerTensa: ArrayAdapter<String>
    private lateinit var spinnerObra: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentEditMarcajeBinding.inflate(inflater, container, false)

        //BORRAR SI NO VA BIEN ES TIME PICKER
        // etName.setOnClickListener { showTimePickerDialog() }


        //SPINNER Actividad-------------------------------------------------------------------------

        spinnerTensa =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item)

        spinnerTensa.addAll(
            Arrays.asList(
                "",
                "OC (Obra Civil)",
                "CT (Centros de Transformación)",
                "AR-IZ (Armado e Izado)",
                "T (Tendido)",
                "RA (Redes Aereas MT/BT)",
                "RS (Redes Subterráneas)",
                "TT (Trabajos en Tensión)",
                "AV.MT/BT (Averías MT/BT)",
                "AV.AT (Averías AT)",
                "Desp (Desplazamiento)"
            )
        )

        mBinding.SpinnerActividades.onItemSelectedListener = this
        mBinding.SpinnerActividades.adapter = spinnerTensa

//--------------------------------------------------------------------------------------------------
        //SPINNER OBRA
        spinnerObra =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item)

        spinnerObra.addAll(
            Arrays.asList(
                "",
                "Obra1",
                "Obra2",
                "Obra3",
                "Obra4",
                "Obra5"
            )
        )
        mBinding.SpinnerTipoObra.onItemSelectedListener = this
        mBinding.SpinnerTipoObra.adapter = spinnerObra


//LANZAR TIME PICKER--------------------------------------------------------------------------------
        mBinding.etName.setOnClickListener { launchTimePicker() }
//--------------------------------------------------------------------------------------------------

        // SI COMENTO ESTO NO DEJA VALIDAR EL FRAGMENT DE AÑADIR MARCAJE
        return mBinding.root

    }

    //----------------------------------------------------------------------------------------------
//GUARDAR TEXT VIEW CON SALIDA SELECCIONADA SPINNER-------------------------------------------------
    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

        /* var actividadSelecionada = spinnerTensa.getItem(position)
          mBinding.tvSpinnerActividad.text = actividadSelecionada*/

        /*  var obraSeleccionada=spinnerObra.getItem(position)
          mBinding.tvSpinnerTipoObra.text=obraSeleccionada*/
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    //--------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong(getString(R.string.arg_id), 0)


        if (id != null && id != 0L) {
            mIsEditMode = true
            getMarcaje(id)
        } else {
            mIsEditMode = false
            mMarcajesEntity = MarcajesEntity(name = "", matricula = "")//, tipoActividad = "")
        }

        setupActionBar()

        setHasOptionsMenu(true)

        setupTextFields()
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title =
            if (mIsEditMode) getString(R.string.edit_marcaje_title_edit)
            else getString(R.string.edit_marcaje_title_add)
    }

    private fun setupTextFields() {
        with(mBinding) {
            /*  etPhototUrl.addTextChangedListener {
              }*/
            etMatricula.addTextChangedListener {

            }

            etName.addTextChangedListener {

                validateFields(tiName)
                //loadImage(it.toString().trim())
            }

            /*  tvSpinnerActividad.addTextChangedListener {


            }*/

        }
    }

    /*private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPhoto)

    }*/

    private fun getMarcaje(id: Long) {
        doAsync {
            mMarcajesEntity = MarcajeApplication.database.marcajeDao().getMarcajeById(id)
            uiThread {
                if (mMarcajesEntity != null) setUiStore(mMarcajesEntity!!)
            }
        }
    }

    //GUARDAR LOS CAMPOS EN EL EDIT FRAGMENT------------------------------------------------------------
    private fun setUiStore(marcajesEntity: MarcajesEntity) {
        with(mBinding) {
            etName.text = marcajesEntity.name.editable()
            etMatricula.text = marcajesEntity.matricula.editable()
            //  etPhototUrl.text = marcajesEntity.photoUrl.editable()
            // tvSpinnerActividad.text = marcajesEntity.tipoActividad.editable()


        }

    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //AL HACER CLICK EN GUARDAR EN EDIT FRAGMENT----------------------------------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            R.id.action_save -> {
                if (mMarcajesEntity != null && validateFields(mBinding.tiName)) {
                    val marcaje = MarcajesEntity(
                        name = mBinding.etName.text.toString().trim(),
                        matricula = mBinding.etMatricula.text.toString().trim()
                        // tipoActividad = mBinding.tvSpinnerActividad.text.toString().trim()
                    )

                    with(mMarcajesEntity!!) {
                        name = mBinding.etName.text.toString().trim()
                        matricula = mBinding.etMatricula.text.toString().trim()
                        //   photoUrl = mBinding.etPhototUrl.text.toString().trim()
                        //tipoActividad = mBinding.tvSpinnerActividad.text.toString().trim()
                    }

                    doAsync {
                        if (mIsEditMode) MarcajeApplication.database.marcajeDao()
                            .updateMarcaje(mMarcajesEntity!!)
                        else mMarcajesEntity!!.id =
                            MarcajeApplication.database.marcajeDao().addMarcaje(mMarcajesEntity!!)
                        uiThread {
                            hideKeyboard()
                            if (mIsEditMode) {
                                mActivity?.updateMarcaje(mMarcajesEntity!!)

                                Snackbar.make(
                                    mBinding.root, R.string.edit_marcaje_message_update_success,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                mActivity?.addMarcaje(mMarcajesEntity!!)



                                Toast.makeText(
                                    mActivity,
                                    R.string.edit_marcaje_message_save_success,
                                    Toast.LENGTH_SHORT
                                ).show()

                                //Cierra fragment y nos lleva a principal al guardar marcaje
                                mActivity?.onBackPressed()
                            }
                        }
                    }
                }

                true
            }
            else -> super.onOptionsItemSelected(item)

        }
        //return super.onOptionsItemSelected(item)


    }

    //VALIDAR FIELDS eficiente----------------------------------------------------------------------
    private fun validateFields(vararg textFields: TextInputLayout): Boolean {
        var isValid = true

        for (textField in textFields) {
            if (textField.editText?.text.toString().trim().isEmpty()) {
                textField.error = getString(R.string.helper_required)
                textField.editText?.requestFocus()
                isValid = false

            } else textField.error = null
        }
        if (!isValid) Snackbar.make(
            mBinding.root, R.string.edit_marcaje_message_valid,
            Snackbar.LENGTH_SHORT
        ).show()

        return isValid
    }


    //VALIDAR FIELDS--------------------------------------------------------------------------------
    private fun validateFields(): Boolean {
        var isValid = true

        if (mBinding.etName.text.toString().trim().isEmpty()) {
            mBinding.tiName.error = getString(R.string.helper_required)
            mBinding.etName.requestFocus()
            isValid = false

        }

        return isValid

    }

    //ocultar teclado-------------------------------------------------------------------------------
    private fun hideKeyboard() {
        val ocultaTecaldo = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            ocultaTecaldo.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    override fun onDestroyView() {

        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        mActivity?.hideFab(true)
        setHasOptionsMenu(false)
        super.onDestroy()


    }
    //PRUEBA LANZAR TIME PICKER---------------------------------------------------------------------

    private fun launchTimePicker(args: Bundle? = null) {
        val fragment = TimePickerFragment()
        if (args != null) fragment.arguments = args


        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.add(R.id.etName, fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()

    }


//TIME PICKER---------------------------------------------------------------------------------------


    fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(requireActivity().supportFragmentManager, "Time")
        //timePicker.show(childFragmentManager.beginTransaction(),"Time")


    }


    private fun onTimeSelected(time: String) {
        mBinding.etName.setText("Has seleccionado... $time")

    }
}










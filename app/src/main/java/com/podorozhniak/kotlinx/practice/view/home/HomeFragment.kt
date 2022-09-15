package com.podorozhniak.kotlinx.practice.view.home

import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentHomeBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.base.Event
import com.podorozhniak.kotlinx.practice.base.FullscreenDialogFragment
import com.podorozhniak.kotlinx.practice.di.appContext
import com.podorozhniak.kotlinx.practice.extensions.*
import com.podorozhniak.kotlinx.practice.util.MemoryManager
import com.podorozhniak.kotlinx.practice.util.Screen
import com.podorozhniak.kotlinx.practice.util.getDrawable
import com.podorozhniak.kotlinx.practice.util.viewhelper.CustomTextFormatter
import com.podorozhniak.kotlinx.practice.util.viewhelper.CustomTextFormatter.Companion.PHONE_PATTERN
import com.podorozhniak.kotlinx.practice.util.viewhelper.CustomTextWatcher
import com.podorozhniak.kotlinx.practice.view.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        private const val SHAKE_ANIMATION_DURATION = 500L
        private const val DELAY_TO_IMPROVE_PROGRESS_ANIM = 100L
        private const val FAKE_LOADING = 2_000L
    }

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun onCreateViewBinding(view: View) = FragmentHomeBinding.bind(view)

    private val eventExample = MutableLiveData<Event<String>>()
    private val eventExampleObserver = Observer<Event<String>> {
        it.getContentIfNotHandled()?.let { content ->
            toast(content)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFade().apply {
            duration = 150L
        }
        exitTransition = MaterialFadeThrough()
    }

    @ExperimentalStdlibApi
    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventExample.observe(viewLifecycleOwner, eventExampleObserver)

        binding.apply {
            customSwitch.trackDrawable = getDrawable(R.drawable.switch_track_unchecked)

            customSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    switchOn()
                } else switchOff()
            }
            btnAr.onClick {
                openArScene()
            }
            //navigation
            btnClFinals.onClick {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCLFinalsFragment())
            }
            btnClCountdown.onClick {
                /*val navOptions =
                    NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()*/
                findNavController().navigate(R.id.action_homeFragment_to_CLCountdownFragment)
            }
            btnFragmentsTransitionAnimation.onClick {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTransitionsAnimationsFragment())
            }
            btnRippleAnimation.onClick {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRipplesFragment())
            }
            btnFullscreenDialog.onClick {
                val fullscreenDialog = FullscreenDialogFragment()
                //fullscreenDialog.isCancelable = false
                fullscreenDialog.show(this@HomeFragment.childFragmentManager, fullscreenDialog.tag)
            }
            btnMaterialAlertDialog.onClick {
                showMaterialAlertDialog()
            }
            btnSecondActivity.onClick {
                (requireActivity() as MainActivity).openSecondActivity()
            }
            btnThirdActivity.onClick {
                (requireActivity() as MainActivity).openThirdActivityForResult()
            }
            btnServiceActivity.onClick {
                (requireActivity() as MainActivity).openServiceActivity()
            }
            btnBindServiceActivity.onClick {
                (requireActivity() as MainActivity).openBindServiceActivity()
            }
            btnViewPager.onClick {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewPagerFragment())
            }

            //UI changes
            btnChangeStatusBarColor.onClick {
                (requireActivity() as MainActivity).animationStatusBarColor(
                    R.color.white,
                    R.color.purple_500
                )
            }
            btnChangeNavBarColor.onClick {
                (requireActivity() as MainActivity).changeNavBarColor()
            }
            btnHideSystemUi.onClick {
                (requireActivity() as MainActivity).hideSystemUI()
            }
            btnShowFlashAnimation.onClick {
                showFlashAnimation()
            }
            btnShowShakeAnimation.onClick {
                showShakeAnimation()
            }

            //info
            btnGetDisplaySizes.onClick {
                getDisplaySizes()
            }
            btnMemory.onClick {
                checkMemories()
            }
            btnHandler.onClick {
                Handler(Looper.getMainLooper()).postDelayed({
                    toast("from looper")
                }, 3_000)
            }
            var numbers: List<Int>
            btnMeasureTime.onClick {
                lifecycleScope.launch {
                    val elapsedTimeMillis = measureTimeMillis {
                        numbers = buildList {
                            addAll(0..100)
                            shuffle()
                            sortDescending()
                        }
                        delay(1_000)
                    }
                    log("${numbers.first()}") // 100
                    log("(The operation took $elapsedTimeMillis ms)")
                }
            }

            //to get real view sizes in px
            btnTest.post {
                log("viewWidthPx = ${binding.btnTest.width}")
                log("viewHeightPx = ${binding.btnTest.height}")
                //converting in dp
                log("viewWidthDp = ${Screen.convertPxToDp(binding.btnTest.width.toFloat())}")
                log("viewHeightDp = ${Screen.convertPxToDp(binding.btnTest.height.toFloat())}")
            }

            //приклад прграмного створення констрейнтів
            /*val constraintLayout: ConstraintLayout = root
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                R.id.iv_foreground,
                ConstraintSet.TOP,
                R.id.top_doodle_edge,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(constraintLayout)*/

            etFirstName.addTextChangedListener(
                CustomTextWatcher(
                    etFirstName,
                    tiFirstName
                ) { etFirstName, tiFirstName ->
                    etFirstName.isPersonNameValid(tiFirstName)
                })

            etPhoneNumber.addTextChangedListener(
                CustomTextFormatter(
                    etPhoneNumber,
                    tiPhoneNumber,
                    PHONE_PATTERN
                ) { etPhoneNumber, tiPhoneNumber ->
                   etPhoneNumber.isPhoneValid(tiPhoneNumber)
                })

            etPassword.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        requireActivity().hideKeyboard()
                        toast("action done")
                        true
                    }
                    else -> false
                }
            }

            //обробка проведення екрану вгору і вниз і координати
            /*root.setOnTouchListener(OnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(activity, "DOWN!!", Toast.LENGTH_SHORT).show()
                    return@OnTouchListener true
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    Toast.makeText(activity, "UP!!", Toast.LENGTH_SHORT).show()
                    return@OnTouchListener true
                }
                false
            })

            root.setOnTouchListener(OnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_MOVE) {
                    val x = event.x.toString()
                    val y = event.y.toString()
                    log("$x, $y")
                    return@OnTouchListener true
                }
                true
            })*/
        }
        eventExample.observe(viewLifecycleOwner, ::observeEvent)
    }

    private fun observeEvent(event: Event<String>) {
        Snackbar.make(binding.root, event.peekContent(), Snackbar.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(2_000)
            eventExample.value = Event("voila")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        eventExample.removeObserver(eventExampleObserver)
    }

    private fun switchOn() {
        showLoadingOnSwitch()
    }

    private fun switchOff() {
        updateSwitch(false)
    }

    private fun showLoadingOnSwitch() {
        lifecycleScope.launch {
            binding.apply {
                delay(DELAY_TO_IMPROVE_PROGRESS_ANIM)
                customSwitch.isEnabled = false
                progress.visibility = View.VISIBLE
                delay(FAKE_LOADING)
                progress.visibility = View.INVISIBLE
                updateSwitch(true)
            }
        }
    }

    private fun updateSwitch(isOn: Boolean) {
        binding.customSwitch.apply {
            binding.progress.visibility = View.GONE
            isEnabled = true
            if (isOn) {
                isChecked = true
                thumbDrawable = getDrawable(R.drawable.thumb_white_circle_shield)
                trackDrawable = getDrawable(R.drawable.switch_track_checked)
            } else {
                isChecked = false
                trackDrawable = getDrawable(R.drawable.switch_track_unchecked)
                thumbDrawable = getDrawable(R.drawable.thumb_white_circle)
            }
            invalidate()
        }
    }

    private fun getDisplaySizes() {
        log("density via context - ${Screen.getDisplayDensityViaContext(appContext)}")
        log("density - ${Screen.getDisplayDensity()}")

        log("width")
        val widthInPx = (requireActivity() as MainActivity).widthScreenInPx()
        val widthInDp = (requireActivity() as MainActivity).widthScreenInDp()
        log("in px - $widthInPx")
        log("in dp - $widthInDp")

        log("height")
        val heightInPx = (requireActivity() as MainActivity).heightScreenInPx()
        val heightInDp = (requireActivity() as MainActivity).heightScreenInDp()
        log("in px - $heightInPx")
        log("in dp - $heightInDp")
    }

    private fun openArScene() {
        val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
        val intentUri: Uri =
            Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                .appendQueryParameter(
                    "file",
                    //"https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf"
                    "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF-Binary/Duck.glb"
                )
                .appendQueryParameter("mode", "ar_only")
                .build()
        sceneViewerIntent.data = intentUri
        sceneViewerIntent.setPackage("com.google.ar.core")
        startActivity(sceneViewerIntent)
    }

    private fun checkMemories() {
        log("total memory - ${MemoryManager.getTotalInternalMemorySize()}")
        log("available memory - ${MemoryManager.getAvailableInternalMemorySize()}")
        log("${MemoryManager.logMemory()}")
        MemoryManager.checkMemory((appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager))
    }

    private fun showShakeAnimation() {
        ObjectAnimator
            .ofFloat(
                binding.btnShowShakeAnimation,
                "translationX",
                0f,
                25f,
                -25f,
                25f,
                -25f,
                15f,
                -15f,
                6f,
                -6f,
                0f
            )
            .setDuration(SHAKE_ANIMATION_DURATION)
            .start()
    }

    private fun showMaterialAlertDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Title")
            .setMessage("Message")
            .setPositiveButton(getString(R.string.positive)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.negative)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }
}




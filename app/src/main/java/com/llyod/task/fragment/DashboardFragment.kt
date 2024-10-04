package com.llyod.task.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.llyod.domain.model.userregistration.RegisteredUserResponse
import com.llyod.task.R
import com.llyod.task.common.Utils.convertBitmapToFile
import com.llyod.task.common.Utils.loadBitmap
import com.llyod.task.databinding.FragmentDashboardBinding
import com.llyod.task.viewmodel.SharedViewModel
import org.eazegraph.lib.models.PieModel
import java.util.concurrent.Executors


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)


        sharedViewModel.isRegisteredUserResponse.observe(viewLifecycleOwner,::registerSuccessLiveData)
        val mPieChart = _binding!!.piechart
        mPieChart.addPieSlice(PieModel("Claims", 0.5f, Color.parseColor("#FE6DA8")))
        mPieChart.startAnimation();

        return binding.root
    }


    private fun registerSuccessLiveData(registeredUserResponse: RegisteredUserResponse?) {

        if (registeredUserResponse?.registered == true){
            binding.idCardView.tvUserNameR.text = registeredUserResponse.personal_details.name
            binding.idCardView.tvDobR.text = registeredUserResponse.personal_details.dob
            binding.idCardView.tvGenderR.text = registeredUserResponse.personal_details.gender
            binding.idCardView.tvUniqueIdR.text = registeredUserResponse.unique_id
            binding.idCardView.tvContactNoR.text = registeredUserResponse.personal_details.mobile_number
            binding.idCardView.tvPrimaryAggregatorR.text = registeredUserResponse.company_details.firstOrNull()?.comapany_name ?: ""
            binding.idCardView.tvUniqueIdAggregatorR.text = registeredUserResponse.company_details.firstOrNull()?.working_id ?: ""
            binding.idCardView.tvNomineeDetailsR.text = registeredUserResponse.nominee_details.name

            try {
                Glide.with(this)
                    .load(registeredUserResponse.personal_details.profile_image)
                    .into( binding.idCardView.ivUserPhoto)

// Declaring and initializing an Executor and a Handler
                val myExecutor = Executors.newSingleThreadExecutor()
                val myHandler = Handler(Looper.getMainLooper())
                myExecutor.execute {
                    try {
                        registeredUserResponse.personal_details.profile_image.let {
                            val bitmap = loadBitmap(it)
                           val leftImageFile =
                                bitmap?.let { it1 ->
                                    convertBitmapToFile(requireContext(),"gigworker",
                                        it1
                                    )
                                }!!
                        }
                    }catch (exception : Exception) {

                    }


                }

            }catch (e : Exception){

            }
            if (registeredUserResponse?.aggregator_status.equals("pending", ignoreCase = true)){
                binding.statusAggregator.text = "(Pending)"
                binding.aggregatorCircle.background = ResourcesCompat.getDrawable(resources, R.drawable.circle_pending, null)
            } else if (registeredUserResponse?.aggregator_status.equals("Approved", ignoreCase = true)){
                binding.statusAggregator.text = "(Approved)"
                binding.aggregatorCircle.background = ResourcesCompat.getDrawable(resources, R.drawable.circle_fill, null)
            } else {
                binding.linearlayout.visibility = View.GONE
                binding.linearlayout3.visibility = View.VISIBLE
                binding.linearlayout2.visibility = View.GONE
            }
            if (registeredUserResponse?.admin_status.equals("pending", ignoreCase = true)){
                binding.adminAggregator.text = "(Pending)"
                binding.adminCircle.background = ResourcesCompat.getDrawable(resources, R.drawable.circle_pending, null)
            } else  if (registeredUserResponse?.admin_status.equals("Approved", ignoreCase = true)){
                binding.adminAggregator.text = "(Approved)"
                binding.adminCircle.background = ResourcesCompat.getDrawable(resources, R.drawable.circle_fill, null)
            }

        }else {
            binding.linearlayout.visibility = View.GONE
            binding.linearlayout3.visibility = View.VISIBLE
            binding.linearlayout2.visibility = View.GONE
            binding.userInfo.visibility = View.GONE
            binding.userChartLl.visibility = View.GONE
        }




    }

}
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myproject.R // ตรวจสอบให้ตรงกับ package ของแอปจริง
import com.example.myproject.components.TopAppBar
import com.example.myproject.database.TaxSavingProduct
import com.example.myproject.database.TaxSavingViewModel


@Composable
fun TaxSavingScreen(navController: NavHostController) {
    val viewModel: TaxSavingViewModel = viewModel()
    val products by viewModel.products.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)

    // สร้าง scroll state สำหรับการเลื่อนแนวตั้งของทั้งหน้า
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchProducts()
    }

    Scaffold(
        topBar = {
            com.example.myproject.components.TopAppBar(
                navController = navController,
                modifier = Modifier.zIndex(1f)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
                .verticalScroll(scrollState) // เพิ่ม vertical scroll สำหรับทั้งหน้า
        ) {
            TaxSavingProducts(navController = navController)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ผลิตภัณฑ์ลดหย่อนภาษี",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = error ?: "เกิดข้อผิดพลาด", color = Color.Red)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.fetchProducts() }) {
                                Text("ลองใหม่อีกครั้ง")
                            }
                        }
                    }
                }
                products.isNotEmpty() -> {
                    // ไม่ต้องมี verticalScroll ที่นี่ เนื่องจาก RecommendationsSection
                    // มี scroll ของตัวเองแล้ว
                    RecommendationsSection(products = products)
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("ไม่พบข้อมูลผลิตภัณฑ์")
                    }
                }
            }

            // เพิ่มพื้นที่ด้านล่างเพื่อให้เนื้อหาไม่ชิดขอบหน้าจอเมื่อเลื่อน
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TaxSavingProducts(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "ผลิตภัณฑ์ลดหย่อนภาษี",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TaxProductItem(R.drawable.ic_savings, "ประกันชีวิต\nออมทรัพย์") {
                navController.navigate("lifeInsurance")
            }
            TaxProductItem(R.drawable.ic_pension, "ประกันบำนาญ") {
                navController.navigate("pensionInsurance")
            }
            TaxProductItem(R.drawable.ic_health, "ประกันสุขภาพ") {
                navController.navigate("healthInsurance")
            }
            TaxProductItem(R.drawable.ic_rmf, "กองทุน RMF") {
                navController.navigate("rmfFund")
            }
            TaxProductItem(R.drawable.ic_ssf, "กองทุน SSF") {
                navController.navigate("ssfFund")
            }
        }
    }
}

@Composable
fun TaxProductItem(icon: Int, name: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() } // ทำให้ไอคอนสามารถคลิกได้
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = name,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = name, fontSize = 12.sp)
    }
}

@Composable
fun RecommendationsSection(products: List<TaxSavingProduct>) {
    // สร้าง scroll state สำหรับการเลื่อนแนวตั้ง
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp) // จำกัดความสูงสูงสุดเพื่อให้เห็นว่าต้องเลื่อน
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
            .verticalScroll(scrollState) // เพิ่ม vertical scroll
    ) {
        Text(text = "แนะนำสำหรับคุณ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        products.forEach { product ->
            val iconResId = getDrawableResourceByName(product.iconName)
            val context = LocalContext.current

            RecommendationItem(
                iconResId = iconResId,
                title = product.title,
                subtitle = product.subtitle,
                rate = product.rate,
                onClick = {
                    // เปิด URL เมื่อคลิกที่รายการ
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.url))
                    context.startActivity(intent)
                }
            )
        }
    }
}


@Composable
fun RecommendationItem(
    iconResId: Int,
    title: String,
    subtitle: String,
    rate: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // เปลี่ยนจาก Icon เป็น Image
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = title,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Medium)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
                Text(text = rate, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun getDrawableResourceByName(name: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(name, "drawable", context.packageName)
}

@Composable
fun openUrl(url: String) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@Composable
fun LifeInsuranceScreen(navController: NavHostController) {
    InsuranceScreenTemplate(
        navController = navController,
        icon = Icons.Filled.Favorite,
        title = "ประกันชีวิตออมทรัพย์",
        subtitle = "เลือกจากแผนประกันที่เหมาะสมกับคุณ",
        detailsContent = { LifeProductDetails(navController) }
    )
}

@Composable
fun PensionInsuranceScreen(navController: NavHostController) {
    InsuranceScreenTemplate(
        navController = navController,
        icon = Icons.Filled.Home,
        title = "ประกันบำนาญ",
        subtitle = "วางแผนการเงินเพื่อความมั่นคงในอนาคต",
        detailsContent = { PensionProductDetails(navController) }
    )
}

@Composable
fun HealthInsuranceScreen(navController: NavHostController) {
    InsuranceScreenTemplate(
        navController = navController,
        icon = Icons.Filled.Favorite,
        title = "ประกันสุขภาพ",
        subtitle = "คุ้มครองสุขภาพอย่างสมบูรณ์",
        detailsContent = { HealthProductDetails(navController) }
    )
}

@Composable
fun RMFFundScreen(navController: NavHostController) {
    InsuranceScreenTemplate(
        navController = navController,
        icon = Icons.Filled.AccountBalance,
        title = "กองทุน RMF",
        subtitle = "เพิ่มพูนความมั่นคงทางการเงินเพื่อการเกษียณ",
        detailsContent = { RMFProductDetails(navController) }
    )
}

@Composable
fun SSFFundScreen(navController: NavHostController) {
    InsuranceScreenTemplate(
        navController = navController,
        icon = Icons.Filled.ShoppingCart,
        title = "กองทุน SSF",
        subtitle = "ลงทุนเพื่อผลตอบแทนและการลดหย่อนภาษี",
        detailsContent = { SSFProductDetails(navController) }
    )
}

// Template กลาง ใช้ได้กับทุกหน้าประกัน
@Composable
fun InsuranceScreenTemplate(
    navController: NavHostController,
    icon: ImageVector,
    title: String,
    subtitle: String,
    detailsContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$title Icon",
            modifier = Modifier.size(48.dp),
            tint = Color(0xFF6200EE)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        detailsContent()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .width(100.dp)
                .height(40.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = "กลับ", color = Color.White, fontSize = 16.sp)
        }
    }
}

// Details ของแต่ละแบบ
@Composable
fun LifeProductDetails(navController: NavHostController) {
    InsuranceDetailsCard(
        company = "FWD",
        productName = "ประกันชีวิตออมทรัพย์ Easy E-Save 10/5",
        coverage = "฿700,000",
        returnRate = "3.20% ต่อปี",
        annualPayment = "฿80,000"
    )
}

@Composable
fun PensionProductDetails(navController: NavHostController) {
    InsuranceDetailsCard(
        company = "FWD",
        productName = "แผนประกันบำนาญ Future Pension 60/10",
        coverage = "฿1,000,000",
        returnRate = "3.50% ต่อปี",
        annualPayment = "฿100,000"
    )
}

@Composable
fun HealthProductDetails(navController: NavHostController) {
    InsuranceDetailsCard(
        company = "AIA",
        productName = "ประกันสุขภาพ Elite Health Plus",
        coverage = "฿1,800,000",
        returnRate = "-",
        annualPayment = "฿45,000"
    )
}

@Composable
fun RMFProductDetails(navController: NavHostController) {
    InsuranceDetailsCard(
        company = "SCB",
        productName = "กองทุน RMF Equity Growth",
        coverage = "800,000",
        returnRate = "5.25% ต่อปี",
        annualPayment = "฿60,000"
    )
}

@Composable
fun SSFProductDetails(navController: NavHostController) {
    InsuranceDetailsCard(
        company = "TMB",
        productName = "กองทุน SSF Global Equity",
        coverage = "950,000",
        returnRate = "5.80% ต่อปี",
        annualPayment = "฿55,000"
    )
}

// Card กลาง ใช้โชว์ข้อมูลประกัน
@Composable
fun InsuranceDetailsCard(
    company: String,
    productName: String,
    coverage: String,
    returnRate: String,
    annualPayment: String,
) {
    val logoRes = when (company) {
        "FWD" -> R.drawable.logo_fwd
        "AIA" -> R.drawable.logo_aia
        "SCB" -> R.drawable.logo_scb
        "TMB" -> R.drawable.logo_tmb
        else -> throw IllegalArgumentException("Company logo not found")// สำรองกรณีไม่มี
    }


    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo + ชื่อบริษัท
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = "$company Logo",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = company, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(text = productName, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (coverage != "-") Tag(text = "ความคุ้มครอง", color = Color(0xFF34C759))
                if (returnRate != "-") Tag(text = "อัตราผลตอบแทน", color = Color(0xFF34C759))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "ทุนประกัน", fontSize = 14.sp)
                    Text(text = coverage, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "ผลตอบแทนเฉลี่ย", fontSize = 14.sp)
                    Text(text = returnRate, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "จ่ายรายปี", fontSize = 14.sp)
            Text(text = annualPayment, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Handle choose plan */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C759))
            ) {
                Text(text = "เลือกแผนนี้", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}



@Composable
fun Tag(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 12.sp)
    }
}
package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric-chaincode-go/shim"

	sc "github.com/hyperledger/fabric-protos-go/peer"
)

// Define the Smart Contract structure
type TypeSmartContract struct {
}

// Define the Type
type Type struct {
	Type2            string `json:"type2"`
	Type3			 string `json:"type3"`
	Label2			 string `json:"label2"`
	Label3           string `json:"label3"`
}

func (s *TypeSmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

func (s *TypeSmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	
	var type1 = Type{
		Type2:  "1",
		Type3:  "",
		Label2: "Doanh thu từ hoạt động sản xuất",
		Label3: "Khoản thu chính đến từ công việc kinh doanh của doanh nghiệp.",
	}

	var type2 = Type{
		Type2:  "2",
		Type3:  "",
		Label2: "Doanh thu từ chuyển nhượng",
		Label3: "Khoản thu từ chuyển nhượng vốn, chứng khoán, bất động sản, dự án đầu tư hay chuyển nhượng quyền sở hữu.",
	}

	var type3 = Type{
		Type2:  "3",
		Type3:  "",
		Label2: "Doanh thu cho thuê tài sản",
		Label3: "",
	}

	var type4 = Type{
		Type2:  "4",
		Type3:  "",
		Label2: "Doanh thu chuyển nhượng, thanh lý",
		Label3: "",
	}

	var type5 = Type{
		Type2:  "5",
		Type3:  "",
		Label2: "Khoản thu đến từ các khoản nợ",
		Label3: "Khoản thu đến từ các khoản nợ khó đòi đã đòi được hay khoản nợ phải trả không xác định được chủ nợ",
	}

	var type6 = Type{
		Type2:  "6",
		Type3:  "",
		Label2: "Chi phí vật liệu công cụ sử dụng trong quá trình sản xuất, kinh doanh",
		Label3: "",
	}

	var type7 = Type{
		Type2:  "7",
		Type3:  "",
		Label2: "Chi phí tiền lương, tiền công, phụ cấp, các khoản bảo hiểm và các khoản trích trên lương",
		Label3: "Khoản thu từ chuyển nhượng vốn, chứng khoán, bất động sản, dự án đầu tư hay chuyển nhượng quyền sở hữu.",
	}

	var type8 = Type{
		Type2:  "8",
		Type3:  "",
		Label2: "Chi phí khấu hao tài sản cố định",
		Label3: "",
	}

	var type9 = Type{
		Type2:  "9",
		Type3:  "",
		Label2: " Chi phí dịch vụ mua ngoài (điện, nước, điện thoại,…)",
		Label3: "",
	}

	var type10 = Type{
		Type2:  "10",
		Type3:  "",
		Label2: "Chi phí thuê đất",
		Label3: "",
	}

	var type11 = Type{
		Type2:  "11",
		Type3:  "",
		Label2: "Chi phí bằng tiền khác",
		Label3: "",
	}

	
	type1AsBytes, _ := json.Marshal(type1)
	type2AsBytes, _ := json.Marshal(type2)
	type3AsBytes, _ := json.Marshal(type3)
	type4AsBytes, _ := json.Marshal(type4)
	type5AsBytes, _ := json.Marshal(type5)
	type6AsBytes, _ := json.Marshal(type6)
	type7AsBytes, _ := json.Marshal(type7)
	type8AsBytes, _ := json.Marshal(type8)
	type9AsBytes, _ := json.Marshal(type9)
	type10AsBytes, _ := json.Marshal(type10)
	type11AsBytes, _ := json.Marshal(type11)
	
	APIstub.PutState("typelv2:1", type1AsBytes)
	APIstub.PutState("typelv2:2", type2AsBytes)
	APIstub.PutState("typelv2:3", type3AsBytes)
	APIstub.PutState("typelv2:4", type4AsBytes)
	APIstub.PutState("typelv2:5", type5AsBytes)
	APIstub.PutState("typelv2:6", type6AsBytes)
	APIstub.PutState("typelv2:7", type7AsBytes)
	APIstub.PutState("typelv2:8", type8AsBytes)
	APIstub.PutState("typelv2:9", type9AsBytes)
	APIstub.PutState("typelv2:10", type10AsBytes)
	APIstub.PutState("typelv2:11", type11AsBytes)

	//var typelv31 = Type{
	//	Type2:  "1",
	//	Type3:  "1",
	//	Label2: "",
	//	Label3: "Bán hàng, cung cấp dịch vụ",
	//}
	//
	//var typelv32 = Type{
	//	Type2:  "2",
	//	Type3:  "2",
	//	Label2: "",
	//	Label3: "Đầu tư công ty con",
	//}
	//
	//var typelv33 = Type{
	//	Type2:  "3",
	//	Type3:  "3",
	//	Label2: "",
	//	Label3: "Thuế thu nhập DN",
	//}
	//
	//type31AsBytes, _ := json.Marshal(typelv31)
	//type32AsBytes, _ := json.Marshal(typelv32)
	//type33AsBytes, _ := json.Marshal(typelv33)
	//
	//APIstub.PutState("typelv3:1", type31AsBytes)
	//APIstub.PutState("typelv3:2", type32AsBytes)
	//APIstub.PutState("typelv3:3", type33AsBytes)
	
	return shim.Success(nil)
}

func (s *TypeSmartContract) queryAllType2(APIstub shim.ChaincodeStubInterface) sc.Response {

	fields := []string{"_id",
		"type2",
		"label2",
		"label3"}

	query := map[string]interface{}{
		"selector": map[string]interface{}{
			"_id": map[string]interface{}{
				"$regex": "^typelv2:",
			},
		},
		"fields": fields,
	}

	queryBytes, err := json.Marshal(query)
	if err != nil {
		return shim.Error("error marshal")
	}

	//APIstub.GetQueryResult()
	resultsIterator, err := APIstub.GetQueryResult(string(queryBytes))

	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- queryTypesLv2:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())

}

func (s *TypeSmartContract) queryType3ByType2(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	fields := []string{"_id",
		"type2",
		"type3",
		"label3"}

	query := map[string]interface{}{
		"selector": map[string]interface{}{
			"_id": map[string]interface{}{
				"$regex": "^typelv3:",
			},
			"type2": map[string]interface{}{
				"$regex": args[0],
			},
		},
		"fields": fields,
	}

	queryBytes, err := json.Marshal(query)
	if err != nil {
		return shim.Error("error marshal")
	}

	//APIstub.GetQueryResult()
	resultsIterator, err := APIstub.GetQueryResult(string(queryBytes))

	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- queryTypesLv2:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())

}

func (s *TypeSmartContract) createType2(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	var type2 = Type{
		Type2:  args[0],
		Type3:  "",
		Label2: args[1],
		Label3: "",
	}

	typeAsBytes, _ := json.Marshal(type2)
	APIstub.PutState("typelv2:"+args[0], typeAsBytes)

	return shim.Success(nil)
}

func (s *TypeSmartContract) createType3(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	var type3 = Type{
		Type2:  args[0],
		Type3:  args[1],
		Label2: "",
		Label3: args[2],
	}

	typeAsBytes, _ := json.Marshal(type3)
	APIstub.PutState("typelv3:"+args[1], typeAsBytes)

	return shim.Success(nil)
}